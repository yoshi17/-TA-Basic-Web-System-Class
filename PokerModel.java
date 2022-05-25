package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PokerModel {
	/*現在のチップ数　初期値は500*/
	int chips;
	
	/*ペア判定変数*/
	int two;
	int three;
	int four;
	boolean isStraight;
	boolean isRoyal;
	
	/*山札*/
	List<Integer> deckcards;
	
	/*手札*/
	List<Integer> handcards;
	
	/*送信ボタンに表示する文字列*/
	String buttonLabel;
	
	/*プレイヤーへのメッセージ*/
	String message;
	
	/** ゲーム回数 */
	int games;
	/** コンストラクタ */
	public PokerModel() {
		deckcards = new ArrayList<>();
		handcards = new ArrayList<>();
	}
	/** 一連のゲームを開始する */
	public void reset() {
		games = 0;
		chips = 500;
	}
	/** 次のゲームのためにカードを配りなおす */
	public void nextgame() {
		// 52枚の山札を作る
		deckcards.clear();
		for (int i=1; i<=52; i++) {
			deckcards.add(i);
		}
		// 山札の先頭から5枚抜いて手札にする
		handcards.clear();
		Collections.shuffle(deckcards);
		for (int i=0; i<5; i++) {
			int n = deckcards.remove(0);
			handcards.add(n);
		}
		// 残りの場札の枚数とカード番号をコンソールに表示する
		System.out.printf("%d: ", deckcards.size());
		for (int n: deckcards) {
			System.out.printf("%d ", n);
		}
		System.out.println();
		
		message = "交換するカードをチェックしてください";
		buttonLabel = "交換";
		games++;
	}
	/** indexで指定された位置のカードを、山札から補充したカードを置き換える */
	public void change(List<String> index) {
		System.out.println("index="+index);
		for (int i=0; i<5; i++) {
			if (index.contains(Integer.toString(i))) {
				int c = deckcards.remove(0); // 山札の先頭を取り除き、
				handcards.set(i, c); // 手札の指定場所に入れる
			}else {
				
			}
		}
		evaluate();
		buttonLabel = "次のゲーム";
	}
	/** 役の判別を行い、チップを増減させる */
	public void evaluate() {
		countNumber();
		int spade = countSpade();
		int heart = countHeart();
		int diamond = countDiamond();
		int clover = countClover();
		int point = 0;
		if (spade == 5 || heart == 5 || diamond == 5 || clover == 5) {
			if (isRoyal) {
				message = "ロイヤルストレートフラッシュ";
				point = 900;
			} else if (isStraight == false) {
				message = "フラッシュ";
				point = 500; 
			} else {
				message = "ストレートフラッシュ";
				point = 800;
			}
		} else if (isStraight) {
			message = "ストレート";
			point = 400;
		} else if (two > 0 && three > 0) {
			message = "フルハウス";
			point = 600;
		} else if (two == 1){
			message = "ワンペア";
			point = 100;
		} else if (two == 2) {
			message = "ツーペア";
			point = 200;
		} else if (three > 0) {
			message = "スリーカード";
			point = 300;
		} else if (four > 0) {
			message = "フォーカード";
			point = 700;
		} else {
			message = "ハイカード";
			point = -100;
		}
		chips += point;
		message += ": " + chips;
	}

	/** 7 の枚数を返す */
	int countSeven() {
		int result = 0;
		for (int hand: handcards) {
			if ((hand-1)%13==6){
				result++;
			}
		}
		return result;
	}

	/** 赤の枚数を返す */
	int countRed() {
		int result = 0;
		for (int hand: handcards) {
			if (13<=hand && hand<=39) {
				result++;
			}
		}
		return result;
	}
	
	int countSpade() {
		int result = 0;
		for (int hand: handcards) {
			if (hand<=13) {
				result++;
			}
		}
		return result;
	}
	
	int countHeart() {
		int result = 0;
		for (int hand: handcards) {
			if (13<hand && hand<=26) {
				result++;
			}
		}
		return result;
	}
	
	int countDiamond() {
		int result = 0;
		for (int hand: handcards) {
			if (26<hand && hand<=39) {
				result++;
			}
		}
		return result;
	}
	
	int countClover() {
		int result = 0;
		for (int hand: handcards) {
			if (39<hand && hand<=52) {
				result++;
			}
		}
		return result;
	}
	/** JSPから呼び出されるメソッド */
	public int getGames() {
		return games;
	}
	/** 現在のチップ数を返す */
	public int getChips() {
		return chips;
	}
	/** 5枚の手札のうち，i番目のカード番号を返す (i=0～4) */
	public int getHandcardAt(int i) {
		return handcards.get(i);
	}
	/** 送信ボタンのラベルを返す */
	public String getButtonLabel() {
		return buttonLabel;
	}
	/** プレイヤーへのメッセージを返す */
	public String getMessage() {
		return message;
	}
	/** 手札をセットする（テスト用） */
	public void setHandcards(int a, int b, int c, int d, int e) {
		handcards.set(0, a);
		handcards.set(1, b);
		handcards.set(2, c);
		handcards.set(3, d);
		handcards.set(4, e);
	}
	/** 数字毎に手札の枚数を数える */
	void countNumber() {
		/** 数字毎に手札を集計した配列(countNumber()内で初期化する) */
		int count[];
		two = 0;
		three= 0;
		four = 0;
		count = new int[13];
		for (int c: handcards) {
			int x = (c-1)%13;
			if (x == 0 || x == 9 || x == 10 || x == 11 || x ==12) {
				isRoyal = true;
			} else {
				isRoyal = false;
			}
			count[x]++;
		}
		for (int n: count) {
			if (n != 1) {
				isStraight = false;
			}
			if (n == 2) {
				two++;
			} else if (n == 3) {
				three++;
			} else if (n == 4) {
				four++;
			}
			System.out.printf("%d ", n);
		}
		System.out.println();
	}

}
