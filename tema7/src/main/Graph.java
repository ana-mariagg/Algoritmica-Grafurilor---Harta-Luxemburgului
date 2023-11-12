package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Graph {
    static Vector<Node> ListaNoduri = new Vector<Node>();
    Vector<Arc> ListaArce = new Vector<Arc>();
    static Vector<Integer> drumFinal = new Vector<Integer>();

    static int startNodeIndex = -1;
    static int endNodeIndex = -1;
    static boolean apelDijkstra = false;

    int nodeDiameter = 15;
    static int window_width;
    static int window_height;

    public static void Init(int width, int height) {
        window_width = width;
        window_height = height;
    }

    public void ReadMap() throws ParserConfigurationException, SAXException, IOException {
        File xmlFile = new File("map.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(xmlFile);

        NodeList documentNodes = document.getElementsByTagName("node");
        int longitudineMax = 0, latidunineMax = 0, longitudineMin = 0, latitudineMin = 0;
        int dim = documentNodes.getLength();
        for (int index = 0; index < dim; index++) {
            org.w3c.dom.Node item = documentNodes.item(index);
            NamedNodeMap elem = item.getAttributes();

            int xPos = Integer.parseInt(elem.getNamedItem("longitude").getNodeValue());
            int yPos = Integer.parseInt(elem.getNamedItem("latitude").getNodeValue());

            if (index == 0) {
                longitudineMin = xPos;
                latitudineMin = yPos;
            }

            if (xPos < longitudineMin)
                longitudineMin = xPos;
            if (yPos < latitudineMin)
                latitudineMin = yPos;

            if (xPos > longitudineMax)
                longitudineMax = xPos;
            if (yPos > latidunineMax)
                latidunineMax = yPos;

            ListaNoduri.add(new Node(xPos, yPos));
        }

        int border = 25;
        dim = ListaNoduri.size();

        for (int index = 0; index < dim; index++) {
            //int float dif= longitudineMax - longitudineMin
            //am coord nod = long nod curent - longMin si impart diferenta la dif. ne poate iesi functie subunitara si ca sa evitam acest lucru inmultim cu inaltimea ferestrei- de 2 ori bordura ca sa avem o marja de siguranta
            //si sa nu ne inceapa exact din margine
            //la fel si pt lat
            //in cazul in care nodul curent e chiar nodul minim, atunci adunam bordura
            int xCoord = border + (int) ((ListaNoduri.get(index).x - longitudineMin) * (window_width - 2 * border) / (float) (longitudineMax - longitudineMin));
            int yCoord = border + (int) ((ListaNoduri.get(index).y - latitudineMin) * (window_height - 2 * border) / (float) (latidunineMax - latitudineMin));

            ListaNoduri.get(index).x = xCoord;
            ListaNoduri.get(index).y = yCoord;
        }

        NodeList documentArcs = document.getElementsByTagName("arc");
        dim = documentArcs.getLength();
        for (int index = 0; index < dim; index++) {
            org.w3c.dom.Node item = documentArcs.item(index);
            NamedNodeMap elem = item.getAttributes();

            //from si to = indexurile nodurilor
            //new Point(ListaNoduri.get(from).x, ListaNoduri.get(from).y) nodul de start pr zis
            //new Point(ListaNoduri.get(to).x, ListaNoduri.get(to).y) nodul de end pr zis
            int from = Integer.parseInt(elem.getNamedItem("from").getNodeValue());
            int to = Integer.parseInt(elem.getNamedItem("to").getNodeValue());
            int cost = Integer.parseInt(elem.getNamedItem("length").getNodeValue());

            ListaArce.add(new Arc(from, to, cost, new Point(ListaNoduri.get(from).x, ListaNoduri.get(from).y), new Point(ListaNoduri.get(to).x, ListaNoduri.get(to).y), index /*indexul arcului din fisier*/));
        }

        //Lista de adiacenta
        dim = ListaArce.size();
        for (int index = 0; index < dim; index++) {
            ListaNoduri.get(ListaArce.get(index).capat1).ArceAdiacente.add(ListaArce.get(index));
        }
    }

    public void NodulCelMaiApropiat(int x, int y) {
        if (startNodeIndex == -1 || endNodeIndex == -1) {
            int dim = ListaNoduri.size();
            int minDistance = Integer.MAX_VALUE;
            int indexDistMin = -1;
            int currentDistance;

            for (int index = 0; index < dim; index++) {
                currentDistance = (int) Math.sqrt(Math.pow(ListaNoduri.get(index).x - x, 2) + Math.pow(ListaNoduri.get(index).y - y, 2));

                if (currentDistance < minDistance) {
                    minDistance = currentDistance;
                    indexDistMin = index;
                }
            }

            if (startNodeIndex == -1) {
                startNodeIndex = indexDistMin;
            } else if (endNodeIndex == -1 && startNodeIndex != indexDistMin) {
                endNodeIndex = indexDistMin;
            }
        }
    }

    public void DrawGraph(Graphics graphicsComponent) {
        int dimArc = ListaArce.size();
        int dimDrum = drumFinal.size();
        for (int index = 0; index < dimArc; index++) {
            graphicsComponent.setColor(Color.BLACK);
            ListaArce.get(index).DrawArc(graphicsComponent);
        }
        if (drumFinal.size() != 0) {
            int previousIndex = 0;
            for (int index = 1; index < dimDrum; index++) {
                graphicsComponent.setColor(Color.RED);
                graphicsComponent.drawLine(ListaNoduri.get(drumFinal.get(previousIndex)).x, ListaNoduri.get(drumFinal.get(previousIndex)).y, ListaNoduri.get(drumFinal.get(index)).x, ListaNoduri.get(drumFinal.get(index)).y);
                previousIndex = index;
            }
        }

        if (startNodeIndex != -1) {
            graphicsComponent.setColor(Color.RED);
            graphicsComponent.fillOval(ListaNoduri.get(startNodeIndex).x - nodeDiameter / 2, ListaNoduri.get(startNodeIndex).y - nodeDiameter / 2, nodeDiameter, nodeDiameter);
        }
        if (endNodeIndex != -1) {
            graphicsComponent.setColor(Color.RED);
            graphicsComponent.fillOval(ListaNoduri.get(endNodeIndex).x - nodeDiameter / 2, ListaNoduri.get(endNodeIndex).y - nodeDiameter / 2, nodeDiameter, nodeDiameter);
        }
    }

    public static boolean ApelDijkstra() {
        if (apelDijkstra == false && startNodeIndex != -1 && endNodeIndex != -1) {
            apelDijkstra = true;
            Dijkstra();
            return true;
        }
        return false;
    }

    public static void Dijkstra() {
        boolean[] vizitate = new boolean[ListaNoduri.size()];
        int[] distanta = new int[ListaNoduri.size()];
        Arrays.fill(distanta, Integer.MAX_VALUE);
        int[] predecesori = new int[ListaNoduri.size()];

        distanta[startNodeIndex] = 0;
        predecesori[startNodeIndex] = -1;

        int distMin;
        int indexNodCurent = startNodeIndex;
        int nrNoduriVizitate = 0;
        Arc arcCurent;
        int dim = ListaNoduri.size();

        while (nrNoduriVizitate < dim) {
            distMin = Integer.MAX_VALUE;
            indexNodCurent = -1;
            for (int index = 0; index < dim; index++) {
                //vizitat= s-au verificat toate nodurile care pleaca din el
                if (vizitate[index] == false && distanta[index] < distMin) {
                    //setez nodul de start
                    indexNodCurent = index;
                    distMin = distanta[index];
                }
            }

            //se actualizeaza vect de vizitate si nr nod curent
            vizitate[indexNodCurent] = true;
            nrNoduriVizitate++;

            //daca e nodul la care trebuia sa ajung e bine
            if (indexNodCurent == endNodeIndex)
                break;

            //daca nu, continui
            //merg pe lista de adiacenta a nodului curent si tin minte fiecare arc de acolo in ArcCurent
            for (int index = 0; index < ListaNoduri.get(indexNodCurent).ArceAdiacente.size(); index++) {
                arcCurent = ListaNoduri.get(indexNodCurent).ArceAdiacente.get(index);
                //la mine se aduna tot pana in punctul ala si se aduna costul si se verifica daca e mai mica decat distanta pe care o avea el deja (fie infinit fie alt nr daca se putea ajunge deja in nodul acela prin alt nod)
                if (arcCurent.cost + distanta[indexNodCurent] < distanta[arcCurent.capat2]) {
                    //daca da, atunci se reactualizeaza distanta si predecesorii
                    distanta[arcCurent.capat2] = arcCurent.cost + distanta[indexNodCurent];
                    predecesori[arcCurent.capat2] = indexNodCurent;
                }
            }
        }

        //imi merge invers pe vect de predecesori de la coada la cap si se adauga arcele mergand pe predecesori in variabila drumFinal
        //in drumFinal am doar indecsii nodurilor care formeaza drumul final de la coada la cap
        int indexPredecesor = endNodeIndex;
        while (predecesori[indexPredecesor] != -1) {
            drumFinal.add(indexPredecesor);
            indexPredecesor = predecesori[indexPredecesor];
        }
        drumFinal.add(startNodeIndex);
    }
}