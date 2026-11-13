
package edu.demo.voting;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlStore {

    private static Path usersFile;
    private static Path votesFile;

    private static final List<String> CANDIDATES =
        Arrays.asList(
            "Candidate A",
            "Candidate B",
            "Candidate C"
        );

    public static synchronized void init(Path directory) {
        try {
            Files.createDirectories(directory);

            usersFile = directory.resolve("users.xml");
            votesFile = directory.resolve("votes.xml");

            if (Files.notExists(usersFile)) {
                writeNew(usersFile, "users");
            }

            if (Files.notExists(votesFile)) {
                writeNew(votesFile, "votes");
            }

            if (!userExists("admin")) {

                Document doc = parse(usersFile);

                Element user = doc.createElement("user");

                user.setAttribute("username", "admin");
                user.setAttribute("name", "Administrator");
                user.setAttribute(
                    "password",
                    PasswordUtil.hash("Admin@123")
                );
                user.setAttribute("role", "admin");

                doc.getDocumentElement().appendChild(user);

                save(doc, usersFile);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeNew(Path file, String root)
            throws Exception {

        Document doc = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .newDocument();

        doc.appendChild(doc.createElement(root));

        save(doc, file);
    }

    private static Document parse(Path file)
            throws Exception {

        DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();

        factory.setFeature(
            "http://apache.org/xml/features/disallow-doctype-decl",
            true
        );

        factory.setExpandEntityReferences(false);

        return factory.newDocumentBuilder().parse(file.toFile());
    }

    private static void save(Document doc, Path file)
            throws Exception {

        Transformer transformer =
            TransformerFactory.newInstance().newTransformer();

        transformer.setOutputProperty(
            OutputKeys.INDENT, "yes"
        );

        transformer.transform(
            new DOMSource(doc),
            new StreamResult(file.toFile())
        );
    }

    public static synchronized boolean userExists(
        String username
    ) throws Exception {

        if (usersFile == null) return false;

        NodeList list = parse(usersFile)
            .getDocumentElement()
            .getElementsByTagName("user");

        for (int i = 0; i < list.getLength(); i++) {

            Element e = (Element) list.item(i);

            if (e.getAttribute("username")
                .equalsIgnoreCase(username)) {
                return true;
            }
        }

        return false;
    }

    public static synchronized boolean register(
        String username,
        String name,
        String password
    ) throws Exception {

        if (username == null || name == null ||
            password == null) {
            return false;
        }

        username = username.trim();
        name = name.trim();

        if (username.isBlank()
            || name.isBlank()
            || password.length() < 8
            || userExists(username)) {
            return false;
        }

        Document doc = parse(usersFile);

        Element user = doc.createElement("user");

        user.setAttribute("username", username);
        user.setAttribute("name", name);
        user.setAttribute(
            "password",
            PasswordUtil.hash(password)
        );
        user.setAttribute("role", "voter");

        doc.getDocumentElement().appendChild(user);

        save(doc, usersFile);

        return true;
    }

    public static synchronized String authenticate(
        String username,
        String password
    ) throws Exception {

        if (username == null || password == null) {
            return null;
        }

        NodeList list = parse(usersFile)
            .getDocumentElement()
            .getElementsByTagName("user");

        for (int i = 0; i < list.getLength(); i++) {

            Element e = (Element) list.item(i);

            if (e.getAttribute("username")
                .equalsIgnoreCase(username.trim())
                && PasswordUtil.verify(
                    password,
                    e.getAttribute("password")
                )) {

                return e.getAttribute("role");
            }
        }

        return null;
    }

    public static synchronized boolean hasVoted(
        String username
    ) throws Exception {

        NodeList list = parse(votesFile)
            .getDocumentElement()
            .getElementsByTagName("vote");

        for (int i = 0; i < list.getLength(); i++) {

            Element e = (Element) list.item(i);

            if (e.getAttribute("username")
                .equalsIgnoreCase(username)) {
                return true;
            }
        }

        return false;
    }

    public static synchronized boolean castVote(
        String username,
        String candidate
    ) throws Exception {

        if (username == null || candidate == null) {
            return false;
        }

        if (hasVoted(username)) {
            return false;
        }

        if (!CANDIDATES.contains(candidate)) {
            return false;
        }

        Document doc = parse(votesFile);

        Element vote = doc.createElement("vote");

        vote.setAttribute("username", username);
        vote.setAttribute("candidate", candidate);

        doc.getDocumentElement().appendChild(vote);

        save(doc, votesFile);

        return true;
    }

    public static synchronized Map<String, Integer> results()
            throws Exception {

        Map<String, Integer> result = new LinkedHashMap<>();

        for (String candidate : CANDIDATES) {
            result.put(candidate, 0);
        }

        NodeList list = parse(votesFile)
            .getDocumentElement()
            .getElementsByTagName("vote");

        for (int i = 0; i < list.getLength(); i++) {

            Element e = (Element) list.item(i);

            String candidate =
                e.getAttribute("candidate");

            if (result.containsKey(candidate)) {

                result.put(
                    candidate,
                    result.get(candidate) + 1
                );
            }
        }

        return result;
    }

    public static synchronized int totalVotes()
            throws Exception {

        int total = 0;

        for (int count : results().values()) {
            total += count;
        }

        return total;
    }

    public static synchronized int totalVoters()
            throws Exception {

        NodeList list = parse(usersFile)
            .getDocumentElement()
            .getElementsByTagName("user");

        int count = 0;

        for (int i = 0; i < list.getLength(); i++) {

            Element e = (Element) list.item(i);

            if ("voter".equals(e.getAttribute("role"))) {
                count++;
            }
        }

        return count;
    }

    public static synchronized String getWinner()
            throws Exception {

        Map<String, Integer> result = results();

        int maxVotes = 0;

        for (int count : result.values()) {

            if (count > maxVotes) {
                maxVotes = count;
            }
        }

        if (maxVotes == 0) {
            return "No votes yet";
        }

        List<String> winners = new ArrayList<>();

        for (Map.Entry<String, Integer> entry :
                result.entrySet()) {

            if (entry.getValue() == maxVotes) {
                winners.add(entry.getKey());
            }
        }

        if (winners.size() > 1) {
            return "Tie: " + String.join(", ", winners);
        }

        return winners.get(0);
    }
}
