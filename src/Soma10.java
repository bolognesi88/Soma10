import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Soma10 {

	public static final int LINHAS = 200;
	public static final int COLUNAS = 9;
		
	public static Integer[][] numeros = new Integer[LINHAS][COLUNAS];
	
	public static int qtdNumeros = 0;	
	public static int bancasRestantes = 5;
	public static int qtdeNumerosTela = 0;
	
	public static void main(String[] arghs) {
		sorteia(35);
		
		Scanner sc = new Scanner(System.in);
		String entrada;
		do {
			imprime();
			
			System.out.print("? ");
			entrada = sc.nextLine();
			
			if (entrada==null) entrada = "";
			else entrada = entrada.trim();
			
			if ((entrada.length()>0)) {
				jogada(entrada);
			}
		}
		while (entrada.length()>0);
		
		sc.close();
		
		System.out.println("Xau");
	}
	
	/**
	 * Realiza uma jogada a partir das coordenadas especificadas
	 * @param entrada
	 */
	public static void jogada(String entrada) {
		entrada = entrada.toUpperCase();
		
		if (entrada.charAt(0)== '+') {
			if (bancasRestantes > 0) {
				duplicaExistentes();
				bancasRestantes--;
				System.out.println("Tik-Tikaaaaaaaaaaaa...............");
				System.out.println(bancasRestantes+"  bancas restantes");
			}
			return;
		}
		
		int c1,l1,c2,l2;
		
		try {
			c1 = entrada.charAt(0)-'A';
			l1 = entrada.charAt(1)-'0'-1;
			c2 = entrada.charAt(2)-'A';
			l2 = entrada.charAt(3)-'0'-1;
			
			if (c1<0 || c2<0 || c1>=COLUNAS || c2>=COLUNAS) { 
				System.err.println("Não entendi coluna");
				System.err.flush(); // tava imprimindo no lugar errado, misturado com o tabuleiro
				return;
			}
			if (l1<0 || l2<0 || l1>=LINHAS  || l2>=LINHAS)  {
				System.err.println("Não entendi linha");
				System.err.flush(); 
				return;
			}
		}
		catch (Throwable t) {
			System.err.println("Não entendi nada :-0");
			System.err.flush(); 
			return;
		}
		
		
		if (numeros[l1][c1]==null || numeros[l2][c2] ==null) {
			System.err.println("não tem numero aí ow!");
			System.err.flush(); 
			return;
		}
		
		boolean deuMatch = true;
		boolean iguais = numeros[l1][c1] == numeros[l2][c2];
		boolean soma10 = numeros[l1][c1] + numeros[l2][c2] == 10;
		
		// se os números forem iguais ou com soma 10, vamos validar suas posições
		if (numeros[l1][c1] != null && numeros[l2][c2] != null && (iguais || soma10)) {
			// caso o par esteja em uma "quebra de linha", não há mais o que verificar;
			// caso contrário, verifica os demais critérios
			if (!(c1 == 8 && c2 == 0 && l2-l1 == 1) || (c1 == 0 && c2 == 8 && l2-l1 == -1)) {
				//
				if (l1 == l2) { // se os dois estiverem na mesma linha
					int menor = Math.min(c1, c2);
					int maior = Math.max(c1, c2);
					//
					// se tiver algum numero mala obstruindo o caminho
					for (int j=menor+1;j<maior;j++) {
						if (numeros[l1][j] != null) {
							System.err.println("tem número na frente");
							System.err.flush();
							deuMatch = false;
							break;
						}
					}
				} else if (c1 == c2) { // se os dois estiverem na mesma coluna
					int menor = Math.min(l1, l2);
					int maior = Math.max(l1, l2);
					//
					// se tiver algum numero mala obstruindo o caminho
					for (int i=menor+1;i<maior;i++) {
						if (numeros[i][c1] != null) {
							System.err.println("tem número na frente");
							System.err.flush();
							deuMatch = false;
							break;
						}
					}
				} else {
					int menorC = Math.min(c1, c2);
					int maiorC = Math.max(c1, c2);
					int menorL = Math.min(l1, l2);
					int maiorL = Math.max(l1, l2);
					//
					if (maiorL-menorL == maiorC-menorC) { // se os dois estiverem na mesma "diagonal principal"
						//
						// procurando por números obstruindo o caminho
						for (int k=1;k<maiorL-menorL;k++) {
							if (numeros[menorL+k][menorC+k] != null) {
								System.err.println("tem número na frente");
								System.err.flush();
								deuMatch = false;
								break;
							}
						}
					} else if (maiorL-menorL == menorC-maiorC) { // testando a diagonal "secundária" 
						//
						// procurando por números obstruindo o caminho
						for (int k=1;k<maiorL-menorL;k++) {
							if (numeros[menorL+k][menorC-k] != null) {
								System.err.println("tem número na frente");
								System.err.flush();
								deuMatch = false;
								break;
							}
						}
					} else deuMatch = false;
				}
			}
		} else {
			System.err.println("ERRRRRRRROOOOUUUUU");
			System.err.flush();
			deuMatch = false;
		}

		if (deuMatch) {
			if (iguais) System.out.println("Iguais!");
			else System.out.println("Soma 10!!!!");
			//
			numeros[l1][c1] = null;
			numeros[l2][c2] = null;
			//
			qtdeNumerosTela -= 2;
			
			if (qtdeNumerosTela==0) {
				System.out.println("ACABOOOOOUUUU, É TETRAAAAA!!!!!");
			}
			else {
				verificaEEliminaLinhasVazias();
			}
		}
						
	}
	
	/**
	 * Coloca os numeros que ainda estão na tela no final da sequencia de nrnso
	 */
	private static void duplicaExistentes() {
		List<Integer> existentes = new ArrayList<Integer>(qtdeNumerosTela);
		
		for (int l=0; l<LINHAS; l++) {
			for (int c=0; c<COLUNAS; c++) {
			
				if (numeros[l][c]!=null) {
					existentes.add(numeros[l][c]);
				}
			}
		}
		
		// insere no final do tabuleiro
		for (Integer i: existentes) {
			numeros[qtdNumeros / COLUNAS][qtdNumeros % COLUNAS] = i;
			qtdNumeros++;
			qtdeNumerosTela++;
		}
	}

	/**
	 * acrescenta a qtd de números ao tabuleiro do jogo, e avança o contador de nros sorteados
	 */
	public static void sorteia(int qtd) {
		for (int q=0; q<qtd; q++) {
			int numero = 1+(new Random()).nextInt(9);
			
			numeros[qtdNumeros / COLUNAS][qtdNumeros % COLUNAS] = numero;
			qtdNumeros++;
			qtdeNumerosTela++;
		}
	}
	
	/** imprime a tela do jogo no console
	*/
	public static void imprime() {
		// imprime o identificador das colunas
		System.out.print("  ");
		for (int c=0; c<COLUNAS; c++) {
			System.out.print(" ");
			System.out.print((char)('A'+c));
		}
		System.out.println();
		
		DecimalFormat doisDigitosFmt = new DecimalFormat("00");
		
		// imprime as linhas, formatadas
		for (int l=0; l<LINHAS; l++) {
			if (linhaVazia(l)) continue;
			
			System.out.print(doisDigitosFmt.format(l+1));
			for (int c=0; c<COLUNAS; c++) {
				System.out.print(" ");
				
				if (numeros[l][c]!=null) {
					System.out.print(numeros[l][c]);	
				}
				else {
					// vazio
					System.out.print(" ");
				}
				
			}
			System.out.println();
		}
		System.out.println("Qtde números: "+ qtdeNumerosTela +"\n");
	}
	
	/**
	 * Verifica se uma determinada linha do tabuleiro está inteira vazia
	 * @param linha
	 * @return
	 */
	private static boolean linhaVazia(int linha) {
		if (linha>=LINHAS) return true;
	
		for (int c=0; c<COLUNAS; c++) {
			if (numeros[linha][c]!=null) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Verifica as linhas que ficaram vazias após a ultima jogada e puxa tudo pra cima.
	 * 
	 * Pra falar a verdade estou bem perdido com este código, não está elegante, deve
	 * ter maneiras muito melhores de fazer.
	 */
	private static void verificaEEliminaLinhasVazias() {
		// vou percorrer só as linhas que já foram preenchidas até o fim, ou seja, até (qtdNumeros/COLUNAS)
		for (int l=0; l<qtdNumeros/COLUNAS; l++) {
			// uma jogada pode remover até 2 linhas ao memso tempo
			for (int r=1; r<=2; r++) {
				if(linhaVazia(l)) {
					qtdNumeros-=COLUNAS; // decrementa a posição onde vai colocar novos números
					for (int m=l+1; m<LINHAS; m++) {
						copiaLinha(m, m-1);
					}
				}
			}
		}
	}
	
	/**
	 * Perceba que isto só funciona mantendo o contador qtdNumeros válido se a última 
	 * linha (constante LINHAS-1) estiver sempre vazia.
	 * 
	 * @param linhaOrigem
	 * @param linhaDestino
	 */
	private static void copiaLinha(int linhaOrigem, int linhaDestino) {
		if (linhaOrigem>=LINHAS) return;
		if (linhaDestino>=LINHAS) return;
	
		for (int c=0; c<COLUNAS; c++) {
			numeros[linhaDestino][c]=numeros[linhaOrigem][c];
		}
		
	}

}
