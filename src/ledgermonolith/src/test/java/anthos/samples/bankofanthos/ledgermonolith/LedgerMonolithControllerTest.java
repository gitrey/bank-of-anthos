package anthos.samples.bankofanthos.ledgermonolith;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.cache.LoadingCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Deque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LedgerMonolithControllerTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionValidator transactionValidator;

    @Mock
    private JWTVerifier verifier;

    @Mock
    private LoadingCache<String, AccountInfo> ledgerReaderCache;

    @Mock
    private LedgerReader ledgerReader;

    @InjectMocks
    private LedgerMonolithController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddTransaction_success() {
        // Mock JWT verification
        DecodedJWT jwt = mock(DecodedJWT.class);
        when(jwt.getClaim("acct").asString()).thenReturn("1234567890");
        when(verifier.verify(any(String.class))).thenReturn(jwt);

        // Create a sample transaction
        Transaction transaction = new Transaction();
        transaction.setRequestUuid("test-uuid");
        transaction.setFromAccountNum("1234567890");
        transaction.setFromRoutingNum("987654321");
        transaction.setToAccountNum("0987654321");
        transaction.setToRoutingNum("123456789");
        transaction.setAmount(1000);

        // Mock transaction validation
        // when(transactionValidator.validateTransaction(any(), any(), any())).thenReturn(true);

        // Perform the request
        ResponseEntity<?> response = controller.addTransaction("Bearer test-token", transaction);

        // Verify the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("ok", response.getBody());
    }

    @Test
    public void testAddTransaction_unauthorized() {
        // Mock JWT verification to throw an exception
        when(verifier.verify(any(String.class))).thenThrow(new com.auth0.jwt.exceptions.JWTVerificationException("invalid token"));

        // Create a sample transaction
        Transaction transaction = new Transaction();
        transaction.setRequestUuid("test-uuid");
        transaction.setFromAccountNum("1234567890");
        transaction.setFromRoutingNum("987654321");
        transaction.setToAccountNum("0987654321");
        transaction.setToRoutingNum("123456789");
        transaction.setAmount(1000);

        // Perform the request
        ResponseEntity<?> response = controller.addTransaction("Bearer invalid-token", transaction);

        // Verify the response
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("not authorized", response.getBody());
    }

    @Test
    public void testAddTransaction_invalidInput() {
        // Mock JWT verification
        DecodedJWT jwt = mock(DecodedJWT.class);
        when(jwt.getClaim("acct").asString()).thenReturn("1234567890");
        when(verifier.verify(any(String.class))).thenReturn(jwt);

        // Create a sample transaction with invalid amount
        Transaction transaction = new Transaction();
        transaction.setRequestUuid("test-uuid");
        transaction.setFromAccountNum("1234567890");
        transaction.setFromRoutingNum("987654321");
        transaction.setToAccountNum("0987654321");
        transaction.setToRoutingNum("123456789");
        transaction.setAmount(-1000);

        // Mock transaction validation to throw an exception
        //  doThrow(new IllegalArgumentException("invalid amount")).when(transactionValidator).validateTransaction(any(), any(), any());

        // Perform the request
        ResponseEntity<?> response = controller.addTransaction("Bearer test-token", transaction);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // assertEquals("invalid amount", response.getBody());
    }

    // TODO: Add tests for duplicate transaction and insufficient balance


}
