package org.aion.zero.impl.precompiled.contracts;

/*
 * @author Centrys
 */

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import org.aion.crypto.ECKey;
import org.aion.crypto.ECKeyFac;
import org.aion.crypto.HashUtil;
import org.aion.crypto.HashUtil.H256Type;
import org.aion.crypto.ISignature;
import org.aion.precompiled.ContractFactory;
import org.aion.precompiled.ContractInfo;
import org.aion.precompiled.PrecompiledTransactionResult;
import org.aion.precompiled.type.CapabilitiesProvider;
import org.aion.precompiled.type.PrecompiledContract;
import org.aion.precompiled.type.PrecompiledTransactionContext;
import org.aion.types.AionAddress;
import org.aion.util.types.AddressUtils;
import org.aion.zero.impl.precompiled.ExternalStateForTests;
import org.aion.zero.impl.vm.precompiled.ExternalCapabilitiesForPrecompiled;
import org.apache.commons.lang3.RandomUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

public class EDVerifyContractTest {

    private ExternalStateForTests externalStateForTests = ExternalStateForTests.usingDefaultRepository();

    private byte[] txHash = RandomUtils.nextBytes(32);
    private AionAddress origin = new AionAddress(RandomUtils.nextBytes(32));
    private AionAddress caller = origin;

    private long blockNumber = 2000001;

    private long nrgLimit = 20000;
    private byte[] pubKey;

    private int depth = 0;

    @BeforeClass
    public static void setupCapabilities() {
        CapabilitiesProvider.installExternalCapabilities(new ExternalCapabilitiesForPrecompiled());
    }

    @AfterClass
    public static void teardownCapabilities() {
        CapabilitiesProvider.removeExternalCapabilities();
    }

    @Test
    public void shouldReturnSuccessTestingWith256() {
        byte[] input = setupInput();
        PrecompiledTransactionContext ctx =
                new PrecompiledTransactionContext(
                        ContractInfo.ED_VERIFY.contractAddress,
                        origin,
                        caller,
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        txHash,
                        txHash,
                        blockNumber,
                        nrgLimit,
                        depth);

        PrecompiledContract contract = new ContractFactory().getPrecompiledContract(ctx, externalStateForTests);

        assertNotNull(contract);
        PrecompiledTransactionResult result = contract.execute(input, 21000L);
        assertThat(result.getStatus().isSuccess());
        assertThat(Arrays.equals(result.getReturnData(), pubKey));
    }

    @Test
    public void emptyInputTest() {
        byte[] input = new byte[128];

        PrecompiledTransactionContext ctx =
                new PrecompiledTransactionContext(
                        ContractInfo.ED_VERIFY.contractAddress,
                        origin,
                        caller,
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        txHash,
                        txHash,
                        blockNumber,
                        nrgLimit,
                        depth);

        PrecompiledContract contract = new ContractFactory().getPrecompiledContract(ctx, externalStateForTests);

        assertNotNull(contract);
        PrecompiledTransactionResult result = contract.execute(input, 21000L);
        assertThat(result.getStatus().isSuccess());
        assertThat(Arrays.equals(result.getReturnData(), pubKey));
    }

    @Test
    public void incorrectInputTest() {
        byte[] input = setupInput();

        input[22] = (byte) ((int) (input[32]) - 10); // modify sig
        input[33] = (byte) ((int) (input[33]) + 4); // modify sig
        input[99] = (byte) ((int) (input[33]) - 40); // modify sig

        PrecompiledTransactionContext ctx =
                new PrecompiledTransactionContext(
                        ContractInfo.ED_VERIFY.contractAddress,
                        origin,
                        caller,
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        txHash,
                        txHash,
                        blockNumber,
                        nrgLimit,
                        depth);

        PrecompiledContract contract = new ContractFactory().getPrecompiledContract(ctx, externalStateForTests);

        assertNotNull(contract);
        PrecompiledTransactionResult result = contract.execute(input, 21000L);
        assertThat(result.getStatus().isSuccess());
        assertThat(Arrays.equals(result.getReturnData(), AddressUtils.ZERO_ADDRESS.toByteArray()));
    }

    @Test
    public void shouldFailIfNotEnoughEnergy() {

        byte[] input = setupInput();
        PrecompiledTransactionContext ctx =
                new PrecompiledTransactionContext(
                        ContractInfo.ED_VERIFY.contractAddress,
                        origin,
                        caller,
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        txHash,
                        txHash,
                        blockNumber,
                        nrgLimit,
                        depth);

        PrecompiledContract contract = new ContractFactory().getPrecompiledContract(ctx, externalStateForTests);

        PrecompiledTransactionResult result = contract.execute(input, 2999L);
        assertEquals("OUT_OF_NRG", result.getStatus().causeOfError);
    }

    @Test
    public void invalidInputLengthTest() {
        byte[] input = new byte[129]; // note the length is 129
        input[128] = 0x1;

        PrecompiledTransactionContext ctx =
                new PrecompiledTransactionContext(
                        ContractInfo.ED_VERIFY.contractAddress,
                        origin,
                        caller,
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        txHash,
                        txHash,
                        blockNumber,
                        nrgLimit,
                        depth);

        PrecompiledContract contract = new ContractFactory().getPrecompiledContract(ctx, externalStateForTests);

        PrecompiledTransactionResult result = contract.execute(input, 21000L);

        assertEquals("FAILURE", result.getStatus().causeOfError);
    }

    private byte[] setupInput() {
        ECKeyFac.setType(ECKeyFac.ECKeyType.ED25519);
        ECKey ecKey = ECKeyFac.inst().create();
        ecKey =
                ecKey.fromPrivate(
                        Hex.decode(
                                "5a90d8e67da5d1dfbf17916ae83bae04ef334f53ce8763932eba2c1116a62426fff4317ae351bda5e4fa24352904a9366d3a89e38d1ffa51498ba9acfbc65724"));

        pubKey = ecKey.getPubKey();

        byte[] data = "Our first test in AION1234567890".getBytes();

        HashUtil.setType(HashUtil.H256Type.KECCAK_256);
        byte[] hashedMessage = HashUtil.h256(data);
        HashUtil.setType(H256Type.BLAKE2B_256);

        ISignature signature = ecKey.sign(hashedMessage);

        byte[] input = new byte[128];
        System.arraycopy(hashedMessage, 0, input, 0, 32);
        System.arraycopy(pubKey, 0, input, 32, 32);
        System.arraycopy(signature.getSignature(), 0, input, 64, 64);

        return input;
    }
}
