package contabancaria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Testes unitários para a classe Conta.
 *
 * PARTE 1 — Testes de exemplo (Construtor) já estão prontos.
 * Observe o padrão AAA e o uso de @Test e @ParameterizedTest.
 *
 * PARTE 2 — Você deve escrever os testes para os demais métodos
 * seguindo rigorosamente o ciclo TDD: Red → Green → Refactor.
 *
 * Para cada método da classe Conta, crie testes que cubram:
 * ✅ O cenário de sucesso (caminho feliz)
 * ❌ Cada regra de validação (cenários de exceção)
 * 🔄 Casos de borda (valores limites)
 */
class ContaTest {

    // =======================================================
    // PARTE 1 — EXEMPLO GUIADO: Testes do Construtor
    // Observe o padrão Arrange-Act-Assert (AAA)
    // =======================================================

    @Test
    void construtor_DadosValidos_CriaContaCorretamente() {
        // Arrange & Act
        var conta = new Conta("Maria", 100);

        // Assert
        assertEquals("Maria", conta.getTitular());
        assertEquals(100, conta.getSaldo());
        assertTrue(conta.isAtiva());
    }

    @Test
    void construtor_SemSaldoInicial_CriaContaComSaldoZero() {
        // Arrange & Act
        var conta = new Conta("João");

        // Assert
        assertEquals("João", conta.getTitular());
        assertEquals(0, conta.getSaldo());
        assertTrue(conta.isAtiva());
    }

    @Test
    void construtor_TitularNulo_LancaIllegalArgumentException() {
        // Assert — verifica que a exceção é lançada
        assertThrows(IllegalArgumentException.class, () -> new Conta(null));
    }

    @Test
    void construtor_TitularVazio_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta(""));
    }

    @Test
    void construtor_SaldoNegativo_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta("Maria", -50));
    }

    @ParameterizedTest
    @CsvSource({
            "Ana,    0",
            "Carlos, 1000",
            "Beatriz, 0.01"
    })
    void construtor_VariosValoresValidos_CriaContaCorretamente(String titular, double saldo) {
        // Act
        var conta = new Conta(titular, saldo);

        // Assert
        assertEquals(titular, conta.getTitular());
        assertEquals(saldo, conta.getSaldo(), 0.001);
        assertTrue(conta.isAtiva());
    }

    // =======================================================
    // PARTE 2 — ESCREVA OS TESTES ABAIXO (TDD)
    // Lembre-se: escreva o teste PRIMEIRO, veja FALHAR (Red),
    // depois implemente o código para PASSAR (Green),
    // e por fim faça Refactor se necessário.
    // =======================================================

    // =======================================================
    // Testes para depositar
    // Sugestão de testes:
    // - Depósito com valor válido atualiza o saldo
    // - Depósito com valor zero lança IllegalArgumentException
    // - Depósito com valor negativo lança IllegalArgumentException
    // - Depósito em conta inativa lança IllegalStateException
    // =======================================================

    @Test
    void depositar_com_valor_zero() {
        // Arrange
        var conta = new Conta("Robson", 100);

        // Assert + Act
        assertThrows(IllegalArgumentException.class, () -> conta.depositar(0));
    }

    @ParameterizedTest
    @CsvSource({
            "Caio, 0.001, 50",
            "Sena, 100000, 800",
            "Diego, 0, 80"
    })
    void deposito_valor_valido(String titular, double saldo, double deposito) {
        // Arrange
        var conta = new Conta(titular, saldo);
        double saldo_esperado = saldo + deposito;

        // Act
        conta.depositar(deposito);

        // Assert
        assertEquals(saldo_esperado, conta.getSaldo(), 0.001);
    }

    @Test
    void depositovalorNegativo() {
        // Arrange
        var conta = new Conta("Sena", 50);

        // Assert + Act
        assertThrows(IllegalArgumentException.class, () -> conta.depositar(-10));

    }

    @Test
    void depositocontaInativa() {
        // Arrange
        var conta = new Conta("Bode", 0);
        conta.encerrar();

        // Assert + Act
        assertThrows(IllegalStateException.class, () -> conta.depositar(50));
    }

    // =======================================================
    // Testes para sacar
    // Sugestão de testes:
    // - Saque com valor válido atualiza o saldo
    // - Saque com valor maior que saldo lança IllegalStateException
    // - Saque com valor zero lança IllegalArgumentException
    // - Saque com valor negativo lança IllegalArgumentException
    // - Saque em conta inativa lança IllegalStateException
    // =======================================================

    @ParameterizedTest
    @CsvSource({
            "Rust, 800, 700",
            "Joy, 1000, 100",
            "Jacket, 0.01, 0.01"
    })
    void saque_valor_validoAtualiza(String titular, double saldo, double saque) {
        // Arrange
        var conta = new Conta(titular, saldo);
        double novo_saldo = saldo - saque;

        // Act
        conta.sacar(saque);

        // Assert
        assertEquals(novo_saldo, conta.getSaldo(), 0.001);
    }

    @Test
    void saque_valor_maiorSaldo() {
        // Arrange
        var conta = new Conta("Sidney", 80);

        // Assert + Act
        assertThrows(IllegalStateException.class, () -> conta.sacar(100));
    }

    @Test
    void saque_valorZero() {
        // Arrange
        var conta = new Conta("Scarface", 1000);

        // Assert + Act
        assertThrows(IllegalArgumentException.class, () -> conta.sacar(0));
    }

    @Test
    void saque_valorNegativo() {
        // Arrange
        var conta = new Conta("JoJo", 200000);

        // Assert + Act
        assertThrows(IllegalArgumentException.class, () -> conta.sacar(-100));
    }

    @Test
    void saque_contaInativa() {
        // Arrange
        var conta = new Conta("Remina", 0);
        conta.encerrar();

        // Assert + Act
        assertThrows(IllegalStateException.class, () -> conta.sacar(90));
    }

    // =======================================================
    // Testes para transferir
    // Sugestão de testes:
    // - Transferência válida atualiza saldo de ambas as contas
    // - Transferência com saldo insuficiente lança exceção
    // - Transferência com valor zero/negativo lança exceção
    // - Transferência com conta origem inativa lança exceção
    // - Transferência com conta destino inativa lança exceção
    // =======================================================

    @Test
    void transferencia_atualizaSaldo() {
        // Arrange
        var destino = new Conta("Hoxton", 1000);
        var origem = new Conta("Dragan", 2000);

        // Act
        origem.transferir(destino, 1000);

        // Assert
        assertEquals(1000, origem.getSaldo(), 0.001);
        assertEquals(2000, destino.getSaldo(), 0.001);
    }

    @Test
    void transferencia_saldoInsuficiente() {
        // Arrange
        var origem = new Conta("Bain", 0);
        var destino = new Conta("Hila", 3000);

        // Assert + Act
        assertThrows(IllegalStateException.class, () -> origem.transferir(destino, 1000));
    }

    @Test
    void tansferencia_valorErrado() {
        // Arrange
        var origem = new Conta("Clover", 30000);
        var destino = new Conta("Hoxton", 500000);

        // Assert + Act
        assertThrows(IllegalArgumentException.class, () -> origem.transferir(destino, -10));
    }

    @Test
    void transferencia_origemInativa() {
        // Arrange
        var origem = new Conta("Jimmy", 0);
        var destino = new Conta("Jiro", 10000);
        origem.encerrar();

        // Assert + Act
        assertThrows(IllegalStateException.class, () -> origem.transferir(destino, 80));
    }

    @Test
    void transferencia_destinoInativo() {
        // Arrange
        var origem = new Conta("Bonnie", 80000);
        var destino = new Conta("John", 0);
        destino.encerrar();

        // Assert + Act
        assertThrows(IllegalStateException.class, () -> origem.transferir(destino, 100));
    }

    // =======================================================
    // Testes para encerrar
    // Sugestão de testes:
    // - Encerrar conta com saldo zero funciona
    // - Encerrar conta com saldo lança IllegalStateException
    // - Encerrar conta já inativa lança IllegalStateException
    // - Conta encerrada tem isAtiva() == false
    // =======================================================

    @Test
    void encerrar_contaSaldozero() {
        // Arrange
        var conta = new Conta("Eloi", 0);

        // Act
        conta.encerrar();

        // Assert
        assertFalse(conta.isAtiva());
    }

    @Test
    void encerrar_contaSaldo() {
        // Arrange
        var conta = new Conta("Cabra", 100);

        // Assert + Act
        assertThrows(IllegalStateException.class, () -> conta.encerrar());
    }

    @Test
    void encerrar_Inativa() {
        // Arrange
        var conta = new Conta("Dallas", 0);
        conta.encerrar();

        // Assert + Acts
        assertThrows(IllegalStateException.class, () -> conta.encerrar());
    }

    @Test
    void verificar_estadoConta() {
        // Arrange
        var conta = new Conta("Wolf", 0);

        // Act
        conta.encerrar();

        // Assert
        assertFalse(conta.isAtiva());
    }
}
