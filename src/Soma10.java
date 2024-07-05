import java.text.DecimalFormat;
import java.util.Random;

public class Soma10 {

	public static final int LINHAS = 12;
	public static final int COLUNAS = 10;
	
	public static Integer[][] numeros = new Integer[LINHAS][COLUNAS];
	
	public static int qtdNumeros = 0;
	
	public static void main(String[] arghs) {
		sorteia(25);
		imprime();
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
