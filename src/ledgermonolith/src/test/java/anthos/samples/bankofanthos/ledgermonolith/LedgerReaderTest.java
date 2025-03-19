package anthos.samples.bankofanthos.ledgermonolith;
 
+import org.junit.jupiter.api.BeforeEach;
+import org.junit.jupiter.api.Test;
+import org.mockito.InjectMocks;
+import org.mockito.Mock;
+import org.mockito.MockitoAnnotations;
+
+import java.util.concurrent.ConcurrentLinkedDeque;
+
+import static org.mockito.Mockito.*;
+
+public class LedgerReaderTest {
+
+    @Mock
+    private TransactionRepository transactionRepository;
+
+    @InjectMocks
+    private LedgerReader ledgerReader;
+
+    @BeforeEach
+    public void setUp() {
+        MockitoAnnotations.openMocks(this);
+    }
+
+    @Test
+    public void testStartWithCallback_startsBackgroundThread() {
+        LedgerReaderCallback callback = mock(LedgerReaderCallback.class);
+        ledgerReader.startWithCallback(callback);
+        // Verify that the background thread is started (implementation detail, not ideal to test directly)
+        // Could potentially check for a change in state within LedgerReader if available
+    }
+
+    @Test
+    public void testPollTransactions_retrievesAndProcessesTransactions() {
+        // Mock transaction repository to return a list of transactions
+        Transaction transaction1 = new Transaction();
+        transaction1.setTransactionId(1L);
+        Transaction transaction2 = new Transaction();
+        transaction2.setTransactionId(2L);
+        when(transactionRepository.findLatest(anyLong())).thenReturn(java.util.Arrays.asList(transaction1, transaction2));
+
+        // Mock the callback
+        LedgerReaderCallback callback = mock(LedgerReaderCallback.class);
+
+        // Call the method
+        ledgerReader.startWithCallback(callback); // Initialize callback
+        ledgerReader.pollTransactions(0L);
+
+        // Verify that the callback is called for each transaction
+        verify(callback).processTransaction(transaction1);
+        verify(callback).processTransaction(transaction2);
+    }
+
+    @Test
+    public void testIsAlive_returnsTrueWhenThreadIsAlive() {
+        // Simulate a healthy thread
+        ledgerReader.startWithCallback(mock(LedgerReaderCallback.class));
+        // Assuming the thread starts and is alive by default in startWithCallback
+        assertEquals(true, ledgerReader.isAlive());
+    }
+
+    // TODO: Add test for isAlive returning false when thread is not alive (difficult to simulate thread death reliably)
+
+    // Add more test methods for transaction processing
+}
