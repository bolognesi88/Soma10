import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;

public class Soma10 {

	public static final int LINHAS = 200;
	public static final int COLUNAS = 9;
		
	public static Integer[][] numeros = new Integer[LINHAS][COLUNAS];
	
	public static int qtdNumeros = 0;	
	public static int qtdeNumerosTela = 35;
	public static int bancasRestantes = 5;
	
	public static void main(String[] arghs) {
		sorteia(qtdeNumerosTela);
		
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
				sorteia(qtdeNumerosTela);
				qtdeNumerosTela *= 2;
				bancasRestantes--;
			}
			return;
		}
		
		int c1,l1,c2,l2;
		
		c1 = entrada.charAt(0)-'A';
		l1 = entrada.charAt(1)-'0'-1;
		c2 = entrada.charAt(2)-'A';
		l2 = entrada.charAt(3)-'0'-1;
		
		boolean deuMatch = true;
		boolean iguais = numeros[l1][c1] == numeros[l2][c2];
		boolean soma10 = numeros[l1][c1] + numeros[l2][c2] == 10;
		
		// se os números forem iguais ou com soma 10, vamos validar suas posições
		if (numeros[l1][c1] != null && numeros[l2][c2] != null && (iguais || soma10)) {
			//
			if (l1 == l2) { // se os dois estiverem na mesma linha
				int menor = Math.min(c1, c2);
				int maior = Math.max(c1, c2);
				//
				// se tiver algum numero mala obstruindo o caminho
				for (int j=menor+1;j<maior;j++) {
					if (numeros[l1][j] != null) {
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
							deuMatch = false;
							break;
						}
					}
				} // else if () { // testando a diagonal "secundária" }
				else deuMatch = false;
			}
		}
		else {
			System.out.println("ERRRRRRRROOOOUUUUU");
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
}
