/**
 * HW4. Backtracking, solution and maze generation <br>
 * This file contains 2 classes: <br> designed by Jean-Christophe Filliâtre
 * 	- ExtendCell provides a cell of the maze with operations to calculate a path to the exit and generate a maze recursively <br>
 * 	- Maze models a maze.
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class extends and enriches the representation of a cell of the maze. <br>
 * It provides to a cell the operations to: <br>
 * -) find a path to the exit <br>
 * -) Generate a maze recursively
 */
class ExtendedCell extends Cell {

    Cell next;

    public ExtendedCell(Maze maze) {
        super(maze);
        this.next = null;
    }

    // Question 1

    /**
     * Test if there is a path from the current cell to an exit
     *
     * @return true if there is a path from the current cell to an exit
     */
    boolean searchPath() {
        maze.slow();

        // 基本情況：到達出口
        if (this.isExit()) {
            this.setMarked(true);
            return true;
        }

        // 標記自己，表示正在探索此路
        this.setMarked(true);

        // 嘗試每個可通行且未標記的鄰居
        for (Cell neighbor : this.getNeighbors(false)) {
            if (!neighbor.isMarked()) {
                if (neighbor.searchPath()) {
                    return true; // 找到路徑，保持標記
                }
            }
        }

        // 所有方向都失敗，回溯：取消標記
        this.setMarked(false);
        return false;
    }

    // Question 2

    /**
     * generate a perfect maze using recursive backtracking
     */
    void generateRec() {
        maze.slow();

        // 取得所有鄰居（忽略牆壁），並隨機排序
        List<Cell> neighbors = this.getNeighbors(true);
        Collections.shuffle(neighbors);

        // 對每個鄰居，如果它是孤立的（未被連接過），就打通並遞迴
        for (Cell neighbor : neighbors) {
            if (neighbor.isIsolated()) {
                this.breakWall(neighbor);
                neighbor.generateRec();
            }
        }
    }
}

/**
 * this class models a maze
 */
class Maze {

    private int height, width;
    /** the grid (array of cells) representing the maze */
    private Cell[][] grid;

    // Question 3

    /**
     * generate a perfect maze using iterative backtracking
     */
    void generateIter(int selectionMethod) {
        Bag cells = new Bag(selectionMethod);
        cells.add(getFirstCell());

        while (!cells.isEmpty()) {
            slow();

            // (1) 取得當前選中的格子（不移除）
            Cell current = cells.peek();

            // (2) 取得所有鄰居並隨機排序，找第一個 isolated 的
            List<Cell> neighbors = current.getNeighbors(true);
            Collections.shuffle(neighbors);

            boolean found = false;
            for (Cell neighbor : neighbors) {
                if (neighbor.isIsolated()) {
                    current.breakWall(neighbor);
                    cells.add(neighbor);
                    found = true;
                    break; // 找到一個就停，回到步驟(1)
                }
            }

            // (3) 沒有 isolated 鄰居，移除當前格子
            if (!found) {
                cells.pop();
            }
        }
    }

    // Question 4

    /**
     * generate a maze using Wilson's algorithm
     */
    void generateWilson() {
        Random rnd = new Random();

        // 把所有格子放進 list 並 shuffle
        List<Cell> allCells = new ArrayList<>();
        for (int i = 0; i < height; i++) for (
            int j = 0;
            j < width;
            j++
        ) allCells.add(grid[i][j]);
        Collections.shuffle(allCells);

        // (1) 標記第一個格子
        allCells.get(0).setMarked(true);

        // (2) 對剩餘未標記的格子
        for (Cell start : allCells) {
            if (start.isMarked()) continue;

            // (2a) 從 start 開始隨機遊走，直到碰到已標記的格子
            Cell current = start;
            while (!current.isMarked()) {
                List<Cell> neighbors = current.getNeighbors(true); // 忽略牆壁
                Cell next = neighbors.get(rnd.nextInt(neighbors.size()));
                ((ExtendedCell) current).next = next; // 記錄離開方向（覆蓋舊的=消除迴圈）
                current = next;
            }

            // (2b) 沿著 next 指標，標記並打通牆壁
            current = start;
            while (!current.isMarked()) {
                current.setMarked(true);
                Cell nextCell = ((ExtendedCell) current).next;
                current.breakWall(nextCell);
                current = nextCell;
            }
        }

        // 清除所有標記（生成完成後不應留下標記）
        clearMarks();
    }

    /**
     * return the cell with coordinates (i, j)
     *
     * @return the cell with coordinates (i, j)
     */
    Cell getCell(int i, int j) {
        if (
            i < 0 || i >= height || j < 0 || j >= width
        ) throw new IllegalArgumentException("invalid indices");

        return grid[i][j];
    }

    /**
     * return the cell with coordinates (0, 0)
     *
     * @return the cell with coordinates (0, 0)
     */
    Cell getFirstCell() {
        return getCell(0, 0);
    }

    // translate coordinates to cell number
    int coordToInt(int i, int j) {
        if (
            i < 0 || i >= height || j < 0 || j >= width
        ) throw new IndexOutOfBoundsException();

        return i * width + j;
    }

    // translate cell number to coordinates
    Coordinate intToCoord(int x) {
        if (x < 0 || x >= height * width) throw new IndexOutOfBoundsException();

        return new Coordinate(x / width, x % width);
    }

    // slow down the display of the maze if a graphical window is open
    void slow() {
        if (frame == null) return;

        try {
            Thread.sleep(10);
            frame.repaint();
        } catch (InterruptedException e) {}
    }

    private MazeFrame frame;
    private static final int step = 20;

    Maze(int height, int width) {
        this(height, width, true);
    }

    Maze(int height, int width, boolean window) {
        if ((height <= 0) || (width <= 0)) throw new IllegalArgumentException(
            "height and width of a Maze must be positive"
        );

        this.height = height;
        this.width = width;

        grid = new Cell[height][width];

        for (int i = 0; i < height; ++i) for (
            int j = 0;
            j < width;
            ++j
        ) grid[i][j] = new ExtendedCell(this);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (i < height - 1) {
                    grid[i][j].addNeighbor(grid[i + 1][j]);
                    grid[i + 1][j].addNeighbor(grid[i][j]);
                }

                if (j < width - 1) {
                    grid[i][j].addNeighbor(grid[i][j + 1]);
                    grid[i][j + 1].addNeighbor(grid[i][j]);
                }
            }
        }

        grid[height - 1][width - 1].setExit(true);

        if (window) frame = new MazeFrame(grid, height, width, step);
    }

    Maze(String path) throws IOException {
        this(Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8));
    }

    Maze(String path, boolean window) throws IOException {
        this(
            Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8),
            window
        );
    }

    Maze(List<String> lines) {
        this(lines, true);
    }

    Maze(List<String> lines, boolean window) {
        if (lines.size() < 2) throw new IllegalArgumentException(
            "too few lines"
        );

        this.height = Integer.parseInt(lines.get(0));
        this.width = Integer.parseInt(lines.get(1));

        this.grid = new Cell[height][width];
        for (int i = 0; i < height; ++i) for (
            int j = 0;
            j < width;
            ++j
        ) grid[i][j] = new ExtendedCell(this);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (i < height - 1) {
                    grid[i][j].addNeighbor(grid[i + 1][j]);
                    grid[i + 1][j].addNeighbor(grid[i][j]);
                }

                if (j < width - 1) {
                    grid[i][j].addNeighbor(grid[i][j + 1]);
                    grid[i][j + 1].addNeighbor(grid[i][j]);
                }
            }
        }

        grid[height - 1][width - 1].setExit(true);

        int i = 0;
        int j = 0;

        for (String line : lines.subList(2, lines.size())) {
            for (int k = 0; k < line.length(); ++k) {
                switch (line.charAt(k)) {
                    case 'N':
                        grid[i][j].breakWall(grid[i - 1][j]);
                        break;
                    case 'E':
                        grid[i][j].breakWall(grid[i][j + 1]);
                        break;
                    case 'S':
                        grid[i][j].breakWall(grid[i + 1][j]);
                        break;
                    case 'W':
                        grid[i][j].breakWall(grid[i][j - 1]);
                        break;
                    case '*':
                        grid[i][j].setMarked(true);
                        break;
                    default:
                        throw new IllegalArgumentException("illegal character");
                }
            }
            ++j;
            if (j >= width) {
                j = 0;
                ++i;
            }
        }

        if (window) frame = new MazeFrame(grid, height, width, step);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(height);
        sb.append('\n');
        sb.append(width);
        sb.append('\n');

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (i > 0 && grid[i][j].hasPassageTo(grid[i - 1][j])) sb.append(
                    'N'
                );
                if (
                    j < width - 1 && grid[i][j].hasPassageTo(grid[i][j + 1])
                ) sb.append('E');
                if (
                    i < height - 1 && grid[i][j].hasPassageTo(grid[i + 1][j])
                ) sb.append('S');
                if (j > 0 && grid[i][j].hasPassageTo(grid[i][j - 1])) sb.append(
                    'W'
                );
                if (grid[i][j].isMarked()) sb.append('*');
                sb.append('\n');
            }
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Maze)) return false;
        Maze that = (Maze) o;

        return this.toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    boolean isPerfect() {
        UnionFind uf = new UnionFind(height * width);

        // union find cycle detection
        for (int i = 0; i < height; ++i) {
            // horizontal edges
            for (int j = 0; j < width - 1; ++j) {
                if (grid[i][j].hasPassageTo(grid[i][j + 1])) {
                    if (
                        uf.sameClass(coordToInt(i, j), coordToInt(i, j + 1))
                    ) return false;
                    uf.union(coordToInt(i, j), coordToInt(i, j + 1));
                }
            }

            // there are no vertical edges in last row, so we're done
            if (i == height - 1) continue;

            // vertical edges
            for (int j = 0; j < width; ++j) {
                if (grid[i][j].hasPassageTo(grid[i + 1][j])) {
                    if (
                        uf.sameClass(coordToInt(i, j), coordToInt(i + 1, j))
                    ) return false;
                    uf.union(coordToInt(i, j), coordToInt(i + 1, j));
                }
            }
        }

        // check if connected
        return (uf.getSize(0) == height * width);
    }

    void clearMarks() {
        for (Cell[] row : grid) for (Cell c : row) c.setMarked(false);
    }
}
