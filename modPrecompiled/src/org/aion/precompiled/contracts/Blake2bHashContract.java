package org.aion.precompiled.contracts;

import static org.aion.crypto.HashUtil.blake256;

import com.google.common.annotations.VisibleForTesting;
import org.aion.precompiled.PrecompiledTransactionResult;
import org.aion.precompiled.type.PrecompiledContract;
import org.aion.types.TransactionStatus;

/**
 * @author Jay Tseng
 * @author William Zhai
 * @implNote Base on benchmark the keccak256hash and blake2bhash precompiled contract blake2b is 5
 *     times faster then keccak256. Therefore, blake2b modify the energy charge to 1/3 of the
 *     Ethereum keccak256 precompiled contract charge.
 */
public class Blake2bHashContract implements PrecompiledContract {

    private static final long COST = 10L;
    private static final int WORD_LENGTH = 4;
    private static final int NRG_CHARGE_PER_WORD = 2;
    private static final String INPUT_LENGTH_ERROR_MESSAGE = "incorrect size of the input data.";

    public Blake2bHashContract() {}

    /**
     * Returns the hash of given input
     *
     * @param input data input; must be less or equal than 2 MB
     * @return the returned blake2b 256bits hash is in ExecutionResult.getOutput
     */
    public PrecompiledTransactionResult execute(byte[] input, long nrg) {

        // check length
        if (input == null || input.length == 0 || input.length > 2_097_152L) {
            return new PrecompiledTransactionResult(
                    TransactionStatus.nonRevertedFailure("FAILURE"),
                    nrg - COST,
                    INPUT_LENGTH_ERROR_MESSAGE.getBytes());
        }

        long additionalNRG =
                ((long) Math.ceil(((double) input.length - 1) / WORD_LENGTH)) * NRG_CHARGE_PER_WORD;

        // check input nrg
        long nrgLeft = nrg - (COST + additionalNRG);

        if (nrgLeft < 0) {
            return new PrecompiledTransactionResult(TransactionStatus.nonRevertedFailure("OUT_OF_NRG"), 0);
        }

        return blake256Hash(input, nrgLeft);
    }

    private PrecompiledTransactionResult blake256Hash(byte[] input, long nrg) {
        byte[] hash = blake256(input);
        return new PrecompiledTransactionResult(TransactionStatus.successful(), nrg, hash);
    }

    @VisibleForTesting
    static byte[] setupInput(int operation, byte[] inputByteArray) {
        byte[] ret = new byte[1 + inputByteArray.length];
        ret[0] = (byte) operation;
        System.arraycopy(inputByteArray, 0, ret, 1, inputByteArray.length);
        return ret;
    }
}
