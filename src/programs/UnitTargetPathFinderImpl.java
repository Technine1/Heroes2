package com.heroes_task.programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        // Координаты атакующего
        com.heroes_task.programs.CoordinateStorage CoordinateStorage = null;
        int[] startPos = CoordinateStorage.getCoordinates(attackUnit);
        int startX = startPos[0];
        int startY = startPos[1];

        // Координаты цели
        int[] goalPos = CoordinateStorage.getCoordinates(targetUnit);
        int goalX = goalPos[0];
        int goalY = goalPos[1];

        // Если уже на месте
        if (startX == goalX && startY == goalY) {
            return Collections.singletonList(new Edge(startX, startY));
        }

        // Массив заблокированных клеток
        boolean[][] blocked = new boolean[WIDTH][HEIGHT];
        for (Unit u : existingUnitList) {
            int[] pos = CoordinateStorage.getCoordinates(u);
            int ux = pos[0];
            int uy = pos[1];

            // Не блокируем клетку, где стоит цель
            if (!(ux == goalX && uy == goalY)) {
                if (isValid(ux, uy)) {
                    blocked[ux][uy] = true;
                }
            }
        }

        // Если старт или цель вне поля
        if (!isValid(startX, startY) || !isValid(goalX, goalY)) {
            return Collections.emptyList();
        }

        // A*
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        Node[][] parents = new Node[WIDTH][HEIGHT];

        Node startNode = new Node(startX, startY, 0, heuristic(startX, startY, goalX, goalY));
        openList.add(startNode);
        visited[startX][startY] = true;
        parents[startX][startY] = null;

        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};

        Node goalNode = null;
        boolean found = false;

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            int cx = current.x;
            int cy = current.y;

            if (cx == goalX && cy == goalY) {
                found = true;
                goalNode = current;
                break;
            }

            for (int i = 0; i < 4; i++) {
                int nx = cx + dx[i];
                int ny = cy + dy[i];

                if (!isValid(nx, ny)) continue;
                if (blocked[nx][ny]) continue;
                if (!visited[nx][ny]) {
                    visited[nx][ny] = true;
                    parents[nx][ny] = current;

                    int g = current.gCost + 1;
                    int h = heuristic(nx, ny, goalX, goalY);
                    Node neighbor = new Node(nx, ny, g, h);
                    openList.add(neighbor);
                }
            }
        }

        if (!found || goalNode == null) {
            return Collections.emptyList();
        }

        // Восстанавливаем путь
        List<Edge> path = new ArrayList<>();
        Node cur = goalNode;
        while (cur != null) {
            path.add(new Edge(cur.x, cur.y));
            cur = parents[cur.x][cur.y];
        }
        Collections.reverse(path);

        return path;
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    private int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private static class Node {
        int x, y;
        int gCost, hCost;

        Node(int x, int y, int gCost, int hCost) {
            this.x = x;
            this.y = y;
            this.gCost = gCost;
            this.hCost = hCost;
        }

        int getF() {
            return gCost + hCost;
        }
    }
}