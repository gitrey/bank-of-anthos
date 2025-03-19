package anthos.samples.bankofanthos.ledgermonolith;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionValidatorTest {

    @InjectMocks
    private TransactionValidator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateTransaction_invalidAccountNum() {
        Transaction transaction = new Transaction();
        transaction.setFromAccountNum("12345"); // Invalid: too short
        transaction.setFromRoutingNum("987654321");
        transaction.setToAccountNum("0987654321");
        transaction.setToRoutingNum("123456789");
        transaction.setAmount(1000);

        assertThrows(IllegalArgumentException.class, () ->
            validator.validateTransaction("111222333", "0000000000", transaction));
    }

    @Test
    public void testValidateTransaction_invalidRoutingNum() {
        Transaction transaction = new Transaction();
        transaction.setFromAccountNum("1234567890");
        transaction.setFromRoutingNum("98765"); // Invalid: too short
        transaction.setToAccountNum("0987654321");
        transaction.setToRoutingNum("123456789");
        transaction.setAmount(1000);

        assertThrows(IllegalArgumentException.class, () ->
            validator.validateTransaction("111222333", "0000000000", transaction));
    }

    @Test
    public void testValidateTransaction_selfTransaction() {
        Transaction transaction = new Transaction();
        transaction.setFromAccountNum("1234567890");
        transaction.setFromRoutingNum("987654321");
        transaction.setToAccountNum("1234567890"); // Same as fromAccountNum
        transaction.setToRoutingNum("987654321"); // Same as fromRoutingNum
        transaction.setAmount(1000);

        assertThrows(IllegalArgumentException.class, () ->
            validator.validateTransaction("111222333", "1234567890", transaction));
    }

    @Test
    public void testValidateTransaction_invalidAmount() {
        Transaction transaction = new Transaction();
        transaction.setFromAccountNum("1234567890");
        transaction.setFromRoutingNum("987654321");
        transaction.setToAccountNum("0987654321");
        transaction.setToRoutingNum("123456789");
        transaction.setAmount(0); // Invalid: zero amount

        assertThrows(IllegalArgumentException.class, () ->
            validator.validateTransaction("111222333", "1234567890", transaction));

        transaction.setAmount(-1000); // Invalid: negative amount
        assertThrows(IllegalArgumentException.class, () ->
            validator.validateTransaction("111222333", "1234567890", transaction));
    }

    @Test
    public void testValidateTransaction_validTransaction() {
        Transaction transaction = new Transaction();
        transaction.setFromAccountNum("1234567890");
        transaction.setFromRoutingNum("987654321");
        transaction.setToAccountNum("0987654321");
        transaction.setToRoutingNum("123456789");
        transaction.setAmount(1000);

        // Should not throw any exception
        validator.validateTransaction("111222333", "1234567890", transaction);
    }

    // Need to add tests for duplicate transaction and insufficient balance after implementing TransactionValidatorTest
}
