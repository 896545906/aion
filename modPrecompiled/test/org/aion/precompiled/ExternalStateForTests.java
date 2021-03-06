package org.aion.precompiled;

import java.math.BigInteger;
import org.aion.precompiled.type.IPrecompiledDataWord;
import org.aion.precompiled.type.IExternalStateForPrecompiled;
import org.aion.precompiled.type.PrecompiledDataWord;
import org.aion.precompiled.type.PrecompiledDoubleDataWord;
import org.aion.precompiled.util.ByteArrayWrapper;
import org.aion.types.AionAddress;

/**
 * A basic testing implementation of the interface.
 */
public final class ExternalStateForTests implements IExternalStateForPrecompiled {
    private final RepositoryForPrecompiled repository;

    private ExternalStateForTests(RepositoryForPrecompiled repository) {
        this.repository = repository;
    }

    public static ExternalStateForTests usingDefaultRepository() {
        return new ExternalStateForTests(new RepositoryForPrecompiled());
    }

    public static ExternalStateForTests usingRepository(RepositoryForPrecompiled repository) {
        return new ExternalStateForTests(repository);
    }

    @Override
    public void commit() {}

    @Override
    public IExternalStateForPrecompiled newChildExternalState() {
        return new ExternalStateForTests(this.repository);
    }

    @Override
    public void addStorageValue(AionAddress address, IPrecompiledDataWord key, IPrecompiledDataWord value) {
        // We drop all of the leading zero bytes when we have a 16-byte data word as the value.
        // This has always been done, it's a repository storage implementation detail as a kind of
        // compaction optimization.
        byte[] valueBytes = (value instanceof PrecompiledDataWord) ? dropLeadingZeroes(value.copyOfData()) : value.copyOfData();
        this.repository.addStorageRow(address, ByteArrayWrapper.wrap(key.copyOfData()), ByteArrayWrapper.wrap(valueBytes));
    }

    @Override
    public void removeStorage(AionAddress address, IPrecompiledDataWord key) {
        this.repository.removeStorageRow(address, ByteArrayWrapper.wrap(key.copyOfData()));
    }

    @Override
    public IPrecompiledDataWord getStorageValue(AionAddress address, IPrecompiledDataWord key) {
        ByteArrayWrapper byteArray = this.repository.getStorageValue(address, ByteArrayWrapper.wrap(key.copyOfData()));
        return (byteArray == null) ? null : toDataWord(byteArray.toBytes());
    }

    @Override
    public BigInteger getBalance(AionAddress address) {
        return this.repository.getBalance(address);
    }

    @Override
    public void addBalance(AionAddress address, BigInteger amount) {
        this.repository.addBalance(address, amount);
    }

    @Override
    public BigInteger getNonce(AionAddress address) {
        return this.repository.getNonce(address);
    }

    @Override
    public void incrementNonce(AionAddress address) {
        this.repository.incrementNonce(address);
    }

    @Override
    public long getBlockNumber() {
        return 0;
    }

    @Override
    public boolean isFork032Enabled() {
        return true;
    }

    @Override
    public boolean isValidEnergyLimitForCreate(long energyLimit) {
        return true;
    }

    @Override
    public boolean isValidEnergyLimitForNonCreate(long energyLimit) {
        return true;
    }

    @Override
    public boolean accountNonceEquals(AionAddress address, BigInteger nonce) {
        return this.repository.getNonce(address).equals(nonce);
    }

    @Override
    public boolean accountBalanceIsAtLeast(AionAddress address, BigInteger balance) {
        return this.repository.getBalance(address).compareTo(balance) >= 0;
    }

    @Override
    public void deductEnergyCost(AionAddress address, BigInteger energyCost) {
        this.repository.addBalance(address, energyCost.negate());
    }

    private static int findIndexOfFirstNonZeroByte(byte[] bytes) {
        int indexOfFirstNonZeroByte = 0;
        for (byte singleByte : bytes) {
            if (singleByte != 0x0) {
                return indexOfFirstNonZeroByte;
            }
            indexOfFirstNonZeroByte++;
        }
        return indexOfFirstNonZeroByte;
    }

    /**
     * Returns the input bytes but with all leading zero bytes removed.
     *
     * <p>If the input bytes consists of all zero bytes then an array of length 1 whose only byte is
     * a zero byte is returned.
     *
     * @param bytes The bytes to chop.
     * @return the chopped bytes.
     */
    private static byte[] dropLeadingZeroes(byte[] bytes) {
        int indexOfFirstNonZeroByte = findIndexOfFirstNonZeroByte(bytes);

        if (indexOfFirstNonZeroByte == bytes.length) {
            return new byte[1];
        }

        byte[] nonZeroBytes = new byte[bytes.length - indexOfFirstNonZeroByte];
        System.arraycopy(bytes, indexOfFirstNonZeroByte, nonZeroBytes, 0, bytes.length - indexOfFirstNonZeroByte);
        return nonZeroBytes;
    }

    /**
     * Converts bytes to the appropriately sized data word implementation and returns it.
     *
     * @param bytes The bytes to convert.
     * @return the data word.
     */
    private static IPrecompiledDataWord toDataWord(byte[] bytes) {
        // A PrecompiledDataWord can be placed into storage as a byte array whose length is between
        // 1 and 16 inclusive. This is how we can tell whether we have a data word or a double.
        return (bytes.length > PrecompiledDataWord.SIZE) ? PrecompiledDoubleDataWord.fromBytes(bytes) : PrecompiledDataWord.fromBytes(bytes);
    }
}
