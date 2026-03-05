/* HW1. Battle
 * This file contains two classes :
 * 		- Deck represents a pack of cards,
 * 		- Battle represents a battle game.
 */

import java.util.LinkedList;

class Deck { // represents a pack of cards

	
	LinkedList<Integer> cards;
	// The methods toString, hashCode, equals, and copy are used for 
	// display and testing, you should not modify them.

	@Override
	public String toString() {
		return cards.toString();
	}

	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		Deck d = (Deck) o;
		return cards.equals(d.cards);
	}

	Deck copy() {
		Deck d = new Deck();
		for (Integer card : this.cards)
			d.cards.addLast(card);
		return d;
	}

	// constructor of an empty deck
	Deck() {
		cards = new LinkedList<Integer>();
	}

	// constructor from field
	Deck(LinkedList<Integer> cards) {
		this.cards = cards;
	}

	// constructor of a complete sorted deck of cards with nbVals values
	Deck(int nbVals) {
		cards = new LinkedList<Integer>();
		for (int j = 1; j <= nbVals; j++)
			for (int i = 0; i < 4; i++)
				cards.add(j);
	}

	// Question 1

	// takes a card from deck d to put it at the end of the current packet
	int pick(Deck d) {
	//	throw new Error("Method pick(Deck d) to complete (Question 1)");
			if (!d.cards.isEmpty()) {
				int x = d.cards.removeFirst();
				cards.addLast(x);
				return x;
			} else {
				return -1;
			}
	}

	// takes all the cards from deck d to put them at the end of the current deck
	void pickAll(Deck d) {
	//	throw new Error("Method pickAll(Deck d) to complete (Question 1)");
		while (!d.cards.isEmpty()) {
			pick(d);
		}
	}

	// checks if the current packet is valid
	boolean isValid(int nbVals) {
	//	throw new Error("Method isValid(int nbVals) to complete (Question 1)");
	int[] numbers = new int[nbVals];
		for (Integer x : cards) {
			if (x < 1 || x > nbVals || numbers[x - 1] > 3) 
				return false;			
			numbers[x - 1]++;
		}
		return true;
	}

	// Question 2.1

	// chooses a position for the cut
	// chooses a position for the cut
    int cut() {
        if(cards.isEmpty()) {
            return 0;
        }
        int n = cards.size();
        int position = 0;
        
        // 使用二項分布：拋 n 次硬幣，計算正面次數
        for (int i = 0; i < n; i++) {
            if (Math.random() < 0.5) {
                position++;
            }
        }
        
        return position;
    }


	// cuts the current packet in two at the position given by cut()
	Deck split() {
        int cutPosition = cut();
        // 如果牌組為空，返回空牌組
        if (cutPosition == -1) {
            return new Deck();
        }
        // 將後半部分牌移到新牌組
        Deck newDeck = new Deck();
        
        // 將前 cutPosition 張牌移到新牌組
        for (int i = 0; i < cutPosition; i++) {
            newDeck.cards.addLast(cards.removeFirst());
        }
        
        return newDeck;
    }

	// Question 2.2

	// mixes the current deck and the deck d
    void riffleWith(Deck d) {
        LinkedList<Integer> result = new LinkedList<Integer>();
        
        // 交錯地從兩個牌組取牌
        while (!cards.isEmpty() || !d.cards.isEmpty()) {
            // 隨機決定先從哪個牌組取牌（模擬真實的riffle shuffle）
            if (!cards.isEmpty() && (d.cards.isEmpty() || Math.random() < 0.5)) {
                result.addLast(cards.removeFirst());
            } else if (!d.cards.isEmpty()) {
                result.addLast(d.cards.removeFirst());
            }
        }
        
        cards = result;
    }


	// Question 2.3

	// shuffles the current deck using the riffle shuffle
	void riffleShuffle(int m) {
        for (int i = 0; i < m; i++) {
            Deck half = split();  // 切牌成兩堆
            riffleWith(half);     // 將兩堆交錯混合
        }
    }
}

class Battle { // represents a battle game

	Deck player1;
	Deck player2;
	Deck trick;
	boolean turn;
	// constructor of a battle without cards
	Battle() {
		player1 = new Deck();
		player2 = new Deck();
		trick = new Deck();
		turn = true;
	}
	
	// constructor from fields
	Battle(Deck player1, Deck player2, Deck trick) {
		this.player1 = player1;
		this.player2 = player2;
		this.trick = trick;
		turn = true;
	}

	// copy the battle
	Battle copy() {
		Battle r = new Battle();
		r.player1 = this.player1.copy();
		r.player2 = this.player2.copy();
		r.trick = this.trick.copy();
		r.turn = this.turn;
		return r;
	}

	// string representing the battle
	@Override
	public String toString() {
		return "Player 1 : " + player1.toString() + "\n" + "Player 2 : " + player2.toString() + "\nPli " + trick.toString();
	}

	// Question 3.1

	// constructor of a battle with a deck of cards of nbVals values
	Battle(int nbVals) {
    	// 1. 建立新的牌組（4*nbVals 張牌）
        Deck fullDeck = new Deck(nbVals);
        
        // 2. 洗牌 7 次
        fullDeck.riffleShuffle(7);
        
        // 3. 初始化玩家牌組和 trick
        player1 = new Deck();
        player2 = new Deck();
        trick = new Deck();
        
        // 4. 輪流分牌給兩位玩家
        boolean toPlayer1 = true;
        while (!fullDeck.cards.isEmpty()) {
            if (toPlayer1) {
                player1.pick(fullDeck);
            } else {
                player2.pick(fullDeck);
            }
            toPlayer1 = !toPlayer1;
        }
        
        turn = true;
	}

	// Question 3.2

	// test if the game is over
	boolean isOver() {
	    return player1.cards.isEmpty() || player2.cards.isEmpty();
	}

	// effectue un tour de jeu
	boolean oneRound() {
		// 一開始就沒牌：無法開始這個 fold
		if (player1.cards.isEmpty() || player2.cards.isEmpty()) {
			return false;
		}

		Integer card1;
		Integer card2;

		// Step 1: each player draws a card and puts it in the trick
		if (turn) {
			card1 = trick.pick(player1);
			card2 = trick.pick(player2);
		} else {
			card2 = trick.pick(player2);
			card1 = trick.pick(player1);
		}

		if (card1 == -1 || card2 == -1) return false;

		// Tie handling (“battle”): add one face-down card each (not compared),
		// then return to step 1 (draw/compare again).
		while (card1.equals(card2)) {
			if (player1.cards.isEmpty() || player2.cards.isEmpty()) {
				return false;
			}

			// face-down cards (not compared)
			if (turn) {
				if (trick.pick(player1) == -1) return false;
				if (trick.pick(player2) == -1) return false;
			} else {
				if (trick.pick(player2) == -1) return false;
				if (trick.pick(player1) == -1) return false;
			}

			// back to step 1 (draw/compare)
			if (player1.cards.isEmpty() || player2.cards.isEmpty()) {
				return false;
			}

			if (turn) {
				card1 = trick.pick(player1);
				card2 = trick.pick(player2);
			} else {
				card2 = trick.pick(player2);
				card1 = trick.pick(player1);
			}

			if (card1 == -1 || card2 == -1) return false;
		}

		// Step 2: winner takes trick, preserving trick order
		if (card1 > card2) player1.pickAll(trick);
		else player2.pickAll(trick);

		// turn changes value (only when the fold was successful)
		turn = !turn;
		return true;
	}



	// Question 3.3

	// returns the winner
    int winner() {
        int size1 = player1.cards.size();
        int size2 = player2.cards.size();
        
        if (size1 > size2) {
            return 1; // 玩家1的牌更多
        } else if (size2 > size1) {
            return 2; // 玩家2的牌更多
        } else {
            return 0; // 兩人牌數相同（平手）
        }
    }
    
    // plays a game with a fixed maximum number of moves
    int game(int turns) {
        for (int i = 0; i < turns; i++) {
            if (isOver()) {
                return winner();
            }
            oneRound();
        }
        // 達到回合限制，根據當前牌數決定贏家
        return winner();
    }


	// Question 4.1

	// plays a game without limit of moves, but with detection of infinite games
    int game() {
        Battle turtle = this.copy();  // 龜（原本的 this）
        Battle hare = this.copy();    // 兔（複製品）
        
        while (!turtle.isOver()) {
            // 龜走一步
            turtle.oneRound();
            
            // 兔走兩步
            if (!hare.isOver()) {
                hare.oneRound();
            }
            if (!hare.isOver()) {
                hare.oneRound();
            }
            
            // 檢查兔是否結束
            if (hare.isOver()) {
                // 讓龜繼續走到結束
                while (!turtle.isOver()) {
                    turtle.oneRound();
                }
                // 更新 this 的狀態為龜的最終狀態
                this.player1 = turtle.player1;
                this.player2 = turtle.player2;
                this.trick = turtle.trick;
                return turtle.winner();
            }
            
            // 檢查龜兔是否在同一狀態（偵測循環）
            if (turtle.toString().equals(hare.toString())) {
                // 發現循環，返回 3（無窮遊戲）
                return 3;
            }
        }
        
        // 龜結束了，更新 this 的狀態
        this.player1 = turtle.player1;
        this.player2 = turtle.player2;
        this.trick = turtle.trick;
        return turtle.winner();
    }


	// Question 4.2

	// performs statistics on the number of infinite games
	static void stats(int nbVals, int nbGames) {
		int w1 = 0;
		int w2 = 0;
		int draws = 0;
		int infinite = 0;

		for (int i = 0; i < nbGames; i++) {
			Battle b = new Battle(nbVals);
			int r = b.game();
			if (r == 1) w1++;
			else if (r == 2) w2++;
			else if (r == 0) draws++;
			else if (r == 3) infinite++;
		}

		System.out.println("wins player1 = " + w1);
		System.out.println("wins player2 = " + w2);
		System.out.println("draws = " + draws);
		System.out.println("infinite games = " + infinite);
	}

}
