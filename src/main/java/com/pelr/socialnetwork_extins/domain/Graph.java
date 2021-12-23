package com.pelr.socialnetwork_extins.domain;

import java.util.*;

/**
 * Graph class for representing graphs.
 */

public class Graph {
    private Map<Long, List<Long>> adjLists;

    /**
     * Creates an undirected graph object
     */

    public Graph(){
        initializeGraph();
    }

    /**
     * Necessary graph initialization
     */

    private void initializeGraph(){
        adjLists = new HashMap<>();
    }

    /**
     * Adds an edge to the graph.
     * @param edge - Tuple containing edge members
     */

    public void addEdge(Tuple<Long, Long> edge){
        Long left = edge.getLeft();//.intValue();
        Long right = edge.getRight();//.intValue();

        if(adjLists.get(left) == null){
            adjLists.put(left, new LinkedList<>());
        }

        if(adjLists.get(right) == null){
            adjLists.put(right, new LinkedList<>());
        }

        adjLists.get(left).add(right);
        adjLists.get(right).add(left);
    }

    /**
     * Adds node to graph
     */

    public void addNode(Long node){
        if(adjLists.get(node) == null){
            adjLists.put(node, new LinkedList<>());
        }
    }

    /**
     * Performs Depth First Search on current graph and returns a connected component through componnent parameter.
     * @param node - current node
     * @param visited - Map for checking if a node was visited
     * @param component - connected component that is being built during current DFS
     */

    private void dfsComponent(Long node, Map<Long, Boolean> visited, Graph component){
        visited.put(node, true);

        for(Long neighbour : adjLists.get(node)){
            if(!visited.get(neighbour)){
                component.addEdge(new Tuple<>(node, neighbour));
                dfsComponent(neighbour, visited, component);
            }
        }
    }

    /**
     * Returns a list of graphs representing the connected components of the current graph.
     * @return components - List of connected components
     */

    public List<Graph> getConnectedComponents(){
        List<Graph> components = new ArrayList<>();
        Map<Long, Boolean> visited = new HashMap<>();
        Set<Long> nodes = adjLists.keySet();

        for(Long node : nodes){
            visited.put(node, false);
        }

        for(Long node: nodes){
            if(!visited.get(node)){
                Graph component = new Graph();
                component.addNode(node);
                dfsComponent(node, visited, component);

                components.add(component);
            }
        }

        return components;
    }

    /**
     * Returns size of graph
     * @return size - int, the size of the graph
     */

    public int getSize(){
        return adjLists.keySet().size();
    }

    /**
     * Returns a list of all the neighbours of the specified node.
     * @param node - node
     * @return neighbours - list of neighbours
     */

    public List<Long> getNeighbours(Long node){
        return adjLists.get(node);
    }


    /**
     * Returns the maximum distance from a distance map and its corresponding node index.
     * @param distances - map of distances
     * @return pair - Tuple of (nodeIndex, maxDistance)
     */

    private Tuple<Long, Long> getMaxDistanceAndNode(Map<Long, Long> distances){
        Long maxDistance = 0L;
        Long nodeIndex = 0L;

        for(Long n : adjLists.keySet()){
            if(distances.get(n) > maxDistance){
                maxDistance = distances.get(n);
                nodeIndex = n;
            }
        }

        return new Tuple<>(nodeIndex, maxDistance);
    }

    /**
     * Returns the longest path from a specified node.
     * @param node - specified node
     * @return path - list of all the nodes contained in the path
     */

    private List<Long> getLongestPathFrom(Long node){
        Map<Long, Long> distances = new HashMap<>();
        Map<Long, Long> parents = new HashMap<>();

        Queue<Long> q = new LinkedList<>();

        q.add(node);
        distances.put(node, 0L);
        parents.put(node, null);

        while(!q.isEmpty()){
            Long nextNode = q.poll();

            for(Long neighbour : adjLists.get(nextNode)) {
                if (distances.get(neighbour) == null) {
                    q.add(neighbour);
                    distances.put(neighbour, distances.get(nextNode) + 1);
                    parents.put(neighbour, nextNode);
                }
            }
        }
        Tuple<Long, Long> nodeAndDistance = getMaxDistanceAndNode(distances);

        return recreatePath(nodeAndDistance.getLeft(), parents);
    }

    /**
     * Recreates a path based on a destination node and the parents of all nodes.
     * @param destination - destination node
     * @param parents - map of parrents of all nodes
     * @return path - recreated path
     */

    private List<Long> recreatePath(Long destination, Map<Long, Long> parents){
        LinkedList<Long> path = new LinkedList<>();
        Long current = destination;

        while(current!= null){
            path.addFirst(current);
            current = parents.get(current);
        }

        return path;
    }

    /**
     * Returns the longest path of the current calling object.
     * The calling graph must be connected in order to obtain expected results.
     * @return path - corresponding path
     */

    private List<Long> longestPathOfCurrent(){
        Long randomNode = adjLists.keySet().iterator().next();
        List<Long> longestPathOfComponent;

        if(this.adjLists.keySet().size() == 1){
            longestPathOfComponent = new ArrayList<>();
            longestPathOfComponent.add(randomNode);

            return longestPathOfComponent;
        }

        List<Long> longestPathFromNode = getLongestPathFrom(randomNode);

        Long pathStartPoint = longestPathFromNode.get(longestPathFromNode.size()-1);
        longestPathOfComponent = getLongestPathFrom(pathStartPoint);

        return longestPathOfComponent;
    }

    /**
     * Returns a tuple consisting of the component that contains the longest path out of all the connected
     * components in the calling graph, and also the longest path.
     *
     * @return tuple - Tuple object containing the graph and the path.
     */

    public Tuple<Graph, List<Long>> longestPathComponent(){
        List<Graph> components = getConnectedComponents();
        Graph longestPathComp = null;
        Long maxPathLength = -1L, currentPathLength;

        List<Long> currentPath = null;
        List<Long> longestPath = null;

        for(Graph component : components){
            currentPath = component.longestPathOfCurrent();

            if((currentPathLength = currentPath.size() - 1L) > maxPathLength){
                maxPathLength = currentPathLength;
                longestPathComp = component;
                longestPath = currentPath;
            }
        }

        return new Tuple<>(longestPathComp, longestPath);
    }

    /**
     * Returns all the nodes from the graph.
     * @return nodes - Iterable containing the nodes
     */

    public Iterable<Long> getNodes(){
        return adjLists.keySet();
    }
}
