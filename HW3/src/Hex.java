/* The Hex game
   https://en.wikipedia.org/wiki/Hex_(board_game)
   desigened by Jean-Christophe Filliâtre

   grid size : n*n

   playable cells : (i,j) with 1 <= i, j <= n

   blue edges (left and right) : i=0 or i=n+1, 1 <= j <= n
    red edges (top and bottom) : 1 <= i <= n, j=0 or j=n+1

      note: the four corners have no color

   adjacence :      i,j-1   i+1,j-1

                 i-1,j    i,j   i+1,j

                    i-1,j+1    i,j+1

*/

public class Hex {

  enum Player {
    NOONE, BLUE, RED
  }

  final int n;
  final Player[][] grid;
  final int[] parent;
  final int[] rank;

  final int redTop, redBottom, blueLeft, blueRight;

  Player trait = Player.RED;
  Player win = Player.NOONE;

  // create an empty board of size n*n
  Hex(int n) {
    this.n = n;
    grid = new Player[n + 2][n + 2];
    int total = (n + 2) * (n + 2) + 4;
    parent = new int[total];
    rank = new int[total];
    for (int k = 0; k < total; k++) {
      parent[k] = k;
      rank[k] = 0;
    }

    redTop = total - 4;
    redBottom = total - 3;
    blueLeft = total - 2;
    blueRight = total - 1;

    for (int i = 0; i <= n + 1; i++) {
      for (int j = 0; j <= n + 1; j++) {
        grid[i][j] = Player.NOONE;
      }
    }

    for (int i = 1; i <= n; i++) {
      grid[i][0] = Player.RED;
      grid[i][n + 1] = Player.RED;
    }
    for (int j = 1; j <= n; j++) {
      grid[0][j] = Player.BLUE;
      grid[n + 1][j] = Player.BLUE;
    }
    grid[0][0] = Player.NOONE;
    grid[0][n + 1] = Player.NOONE;
    grid[n + 1][0] = Player.NOONE;
    grid[n + 1][n + 1] = Player.NOONE;

    for (int i = 1; i <= n; i++) {
      union(id(i, 0), redTop);
      union(id(i, n + 1), redBottom);
    }
    for (int j = 1; j <= n; j++) {
      union(id(0, j), blueLeft);
      union(id(n + 1, j), blueRight);
    }
  }

  private int id(int i, int j) {
    return i + (n + 2) * j;
  }

  private int find(int x) {
    if (parent[x] != x) {
      parent[x] = find(parent[x]);
    }
    return parent[x];
  }

  private void union(int a, int b) {
    int ra = find(a);
    int rb = find(b);
    if (ra == rb)
      return;
    if (rank[ra] < rank[rb]) {
      parent[ra] = rb;
    } else if (rank[ra] > rank[rb]) {
      parent[rb] = ra;
    } else {
      parent[rb] = ra;
      rank[ra]++;
    }
  }

  // return the color of cell i,j
  Player get(int i, int j) {
    if (i < 0 || i > n + 1 || j < 0 || j > n + 1)
      return Player.NOONE;
    return grid[i][j];
  }

  // update the board after the player with the trait plays the cell (i, j).
  // Does nothing if the move is illegal.
  // Returns true if and only if the move is legal.
  boolean click(int i, int j) {
    if (win != Player.NOONE)
      return false;
    if (i < 1 || i > n || j < 1 || j > n)
      return false;
    if (grid[i][j] != Player.NOONE)
      return false;

    grid[i][j] = trait;

    int[][] dirs = { { 0, -1 }, { 1, -1 }, { 1, 0 }, { 0, 1 }, { -1, 1 }, { -1, 0 } };
    for (int[] d : dirs) {
      int ni = i + d[0];
      int nj = j + d[1];
      if (ni < 0 || ni > n + 1 || nj < 0 || nj > n + 1)
        continue;
      if (grid[ni][nj] == trait) {
        union(id(i, j), id(ni, nj));
      }
    }

    if (trait == Player.RED) {
      if (find(redTop) == find(redBottom)) {
        win = Player.RED;
      }
    } else if (trait == Player.BLUE) {
      if (find(blueLeft) == find(blueRight)) {
        win = Player.BLUE;
      }
    }

    if (win == Player.NOONE) {
      trait = (trait == Player.RED) ? Player.BLUE : Player.RED;
    }

    return true;
  }

  // return the player with the trait or Player.NOONE if the game is over
  // because of a player's victory.
  Player currentPlayer() {
    return win == Player.NOONE ? trait : Player.NOONE;
  }

  // return the winning player, or Player.NOONE if no player has won yet
  Player winner() {
    return win;
  }

  int label(int i, int j) {
    if (i < 0 || i > n + 1 || j < 0 || j > n + 1)
      return -1;
    return find(id(i, j));
  }


  public static void main(String[] args) {
    HexGUI.createAndShowGUI();
  }
}
