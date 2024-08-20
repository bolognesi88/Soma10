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
		else if (entrada.charAt(0)== '?') {
			// pediu dica
			gerarDica();
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
		
		/*
		 * Separei a lógica de negócio (método abaixo) da interação com o usuário (prints) para poder reaproveitar 
		 * o código na geração de dicas
		 */
		ResultadoJogada resultado = getResultadoJogada(l1,c1,l2,c2);
		
		if (resultado==ResultadoJogada.ERRO_CELULA_VAZIA) {
			System.err.println("não tem numero aí ow!");
			System.err.flush(); 
			return;
		}
		
		if (resultado==ResultadoJogada.ERRO_TEM_NUMERO_NA_FRENTE) {
			System.err.println("tem número na frente");
			System.err.flush();
			return;
		}
		
		if (resultado==ResultadoJogada.ERRO_NUMEROS_INCOMPATIVEIS) {
			System.err.println("ERRRRRRRROOOOUUUUU");
			System.err.flush();
			return;
		}

		if (resultado==ResultadoJogada.MATCH_IGUAIS || resultado == ResultadoJogada.MATCH_SOMA_10) {
			if (resultado==ResultadoJogada.MATCH_IGUAIS) System.out.println("Iguais!");
			else System.out.println("Soma 10!!!!");
			
			mensagemEventualMatch(l1,c1,l2,c2);
			
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
	
	public static enum ResultadoJogada {
		MATCH_IGUAIS,
		MATCH_SOMA_10,
		ERRO_TEM_NUMERO_NA_FRENTE,
		ERRO_NUMEROS_INCOMPATIVEIS,
		ERRO_CELULA_VAZIA;
	}
	
	private static ResultadoJogada getResultadoJogada(int l1, int c1, int l2, int c2) {
		if (l1==l2 && c1==c2) {
			return ResultadoJogada.ERRO_NUMEROS_INCOMPATIVEIS;
		}
		
		if (numeros[l1][c1]==null || numeros[l2][c2] ==null) {
			return ResultadoJogada.ERRO_CELULA_VAZIA;
		}
		
		boolean iguais = numeros[l1][c1] == numeros[l2][c2];
		boolean soma10 = numeros[l1][c1] + numeros[l2][c2] == 10;
		if (!iguais && !soma10) {
			return ResultadoJogada.ERRO_NUMEROS_INCOMPATIVEIS;
		}
		
		boolean temNumeroNaFrente = temNumeroNaFrente(l1, c1, l2,c2);
		if (temNumeroNaFrente) {
			return ResultadoJogada.ERRO_TEM_NUMERO_NA_FRENTE;
		}
		
		if (iguais) return ResultadoJogada.MATCH_IGUAIS;
		else if (soma10) return ResultadoJogada.MATCH_SOMA_10;
		else return null; // ????
	}


	private static void gerarDica() {
		for (int l1=0; l1<LINHAS; l1++) {
			for (int c1=0; c1<COLUNAS; c1++) {
				for (int l2=l1; l2<LINHAS; l2++) {
					for (int c2=c1; c2<COLUNAS; c2++) {
						ResultadoJogada resultado = getResultadoJogada(l1, c1, l2, c2);
						if (resultado==ResultadoJogada.MATCH_IGUAIS) {
							System.out.println("iguais "+((char)(c1+'A'))+""+(l1+1)+""+((char)(c2+'A'))+""+(l2+1));
						}
						else if (resultado==ResultadoJogada.MATCH_SOMA_10) {
							System.out.println("soma10 "+((char)(c1+'A'))+""+(l1+1)+""+((char)(c2+'A'))+""+(l2+1));
						} 
					}
				}		
			}
		}
		
	}	
	
	/**
	 * Refatorei a parte do código que testa se tem algum número entre as posicoes (l1,c1) e (l2,c2).
	 * 
	 * @param l1
	 * @param c1
	 * @param l2
	 * @param c2
	 * @return
	 */
	private static boolean temNumeroNaFrente(int l1, int c1, int l2, int c2) {
		if (l1 == l2) { // se os dois estiverem na mesma linha
			int menor = Math.min(c1, c2);
			int maior = Math.max(c1, c2);
			//
			// se tiver algum numero mala obstruindo o caminho
			for (int j=menor+1;j<maior;j++) {
				if (numeros[l1][j] != null) {
					return true;
				}
			}
		} else if (c1 == c2) { // se os dois estiverem na mesma coluna
			int menor = Math.min(l1, l2);
			int maior = Math.max(l1, l2);
			//
			// se tiver algum numero mala obstruindo o caminho
			for (int i=menor+1;i<maior;i++) {
				if (numeros[i][c1] != null) {
					return true;
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
						return true;
					}
				}
			} else if (maiorL-menorL == menorC-maiorC) { // testando a diagonal "secundária" 
				//
				// procurando por números obstruindo o caminho
				for (int k=1;k<maiorL-menorL;k++) {
					if (numeros[menorL+k][menorC-k] != null) {
						return true;
					}
				}
			} else if (maiorL-menorL == 1) { // testando quebra de linha
				
				// preciso conhecer as posições das colunas dos elementos da linha de cima
				// e da linha de baixo, para poder varrer o espaço entre os dois números,
				// procurando por obstruções
				int colUp = 0;
				int colDown = 0;
				//
				if (l2 > l1) {
					colUp = c1;
					colDown = c2;
				} else {
					colUp = c2;
					colDown = c1;
				}
				
				// procurando por obstruções na primeira linha
				for (int k = colUp+1; k < 9; k++) {
					if (numeros[menorL][k] != null) {
						return true;
					}
				}
				// se já achou obstrução na linha de cima, nem procura na de baixo
				// if (deuMatch) { porque já deu return né , ihhhihihihih
				for (int k = 0; k < colDown; k++) {
					if (numeros[maiorL][k] != null) {
						return true;
					}
				}
				// }
				
			}
			else {
				return true;
			}
		}
		
		// se não encontrou nada é porque não tem numero na frente
		return false;
	}

	/**
	 * Congratulações ao jogador
	 * @param l1
	 * @param c1
	 * @param l2
	 * @param c2
	 */
	private static void mensagemEventualMatch(int l1, int c1, int l2, int c2) {
		int distanciaN1 = Math.abs(l2-l1) + Math.abs(c2-c1); 
		
		if (distanciaN1==3) {
			System.out.println("Excelente!");
		}
		else if (distanciaN1==4) {
			System.out.println("Que sacada!");
		}
		else if (distanciaN1==5) {
			System.out.println("Sois Baita!");
		}
		else if (distanciaN1>=6) {
			System.out.println("Tua cabeça é maior que um balão de ar quente!");
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
