package org.aion.precompiled;

import org.aion.base.type.AionAddress;
import org.aion.mcf.config.CfgFork;
import org.aion.precompiled.contracts.ATB.TokenBridgeContract;
import org.aion.precompiled.contracts.Blake2bHashContract;
import org.aion.precompiled.contracts.EDVerifyContract;
import org.aion.precompiled.contracts.TXHashContract;
import org.aion.precompiled.contracts.TotalCurrencyContract;
import org.aion.precompiled.type.PrecompiledContract;
import org.aion.mcf.vm.types.KernelInterfaceForFastVM;
import org.aion.vm.api.interfaces.TransactionContext;

/** A factory class that produces pre-compiled contract instances. */
public class ContractFactory {

    private static final String ADDR_OWNER =
            "0000000000000000000000000000000000000000000000000000000000000000";
    private static final String ADDR_TOTAL_CURRENCY =
            "0000000000000000000000000000000000000000000000000000000000000100";

    private static final String ADDR_TOKEN_BRIDGE =
            "0000000000000000000000000000000000000000000000000000000000000200";
    private static final String ADDR_TOKEN_BRIDGE_INITIAL_OWNER =
            "a008d7b29e8d1f4bfab428adce89dc219c4714b2c6bf3fd1131b688f9ad804aa";

    private static final String ADDR_ED_VERIFY =
            "0000000000000000000000000000000000000000000000000000000000000010";
    private static final String ADDR_BLAKE2B_HASH =
            "0000000000000000000000000000000000000000000000000000000000000011";
    private static final String ADDR_TX_HASH =
            "0000000000000000000000000000000000000000000000000000000000000012";

    private static PrecompiledContract PC_ED_VERIFY;
    private static PrecompiledContract PC_BLAKE2B_HASH;

    static {
        PC_ED_VERIFY = new EDVerifyContract();
        PC_BLAKE2B_HASH = new Blake2bHashContract();
    }

    public ContractFactory() {}

    /**
     * Returns a new pre-compiled contract such that the address of the new contract is address.
     * Returns null if address does not map to any known contracts.
     *
     * @param context Passed in execution context.
     * @param track The repo.
     * @return the specified pre-compiled address.
     */
    public PrecompiledContract getPrecompiledContract(
            TransactionContext context, KernelInterfaceForFastVM track) {

        CfgFork cfg = new CfgFork();
        String forkProperty = cfg.getProperties().getProperty("fork0.3.2");

        boolean fork_032 =
                (forkProperty != null) && (context.getBlockNumber() >= Long.valueOf(forkProperty));

        switch (context.getDestinationAddress().toString()) {
            case ADDR_TOKEN_BRIDGE:
                TokenBridgeContract contract =
                        new TokenBridgeContract(
                                context,
                                track.getRepositoryCache(),
                                AionAddress.wrap(ADDR_TOKEN_BRIDGE_INITIAL_OWNER),
                                AionAddress.wrap(ADDR_TOKEN_BRIDGE));

                if (!context.getOriginAddress()
                                .equals(AionAddress.wrap(ADDR_TOKEN_BRIDGE_INITIAL_OWNER))
                        && !contract.isInitialized()) {
                    return null;
                }

                return contract;
            case ADDR_ED_VERIFY:
                return fork_032 ? PC_ED_VERIFY : null;
            case ADDR_BLAKE2B_HASH:
                return fork_032 ? PC_BLAKE2B_HASH : null;
            case ADDR_TX_HASH:
                return fork_032 ? new TXHashContract(context) : null;
            case ADDR_TOTAL_CURRENCY:
                return fork_032
                        ? null
                        : new TotalCurrencyContract(
                                track.getRepositoryCache(),
                                context.getSenderAddress(),
                                AionAddress.wrap(ADDR_OWNER));
            default:
                return null;
        }
    }

    /**
     * Returns true if address is the address of a pre-compiled contract and false otherwise.
     *
     * @param address The address to check.
     * @return true iff address is address of a pre-compiled contract.
     */
    public static boolean isPrecompiledContract(AionAddress address) {
        switch (address.toString()) {
            case ADDR_TOKEN_BRIDGE:
            case ADDR_ED_VERIFY:
            case ADDR_BLAKE2B_HASH:
            case ADDR_TX_HASH:
                return true;
            case ADDR_TOTAL_CURRENCY:
            default:
                return false;
        }
    }

    /**
     * Returns the address of the TotalCurrencyContract contract.
     *
     * @return the contract address.
     */
    public static AionAddress getTotalCurrencyContractAddress() {
        return AionAddress.wrap(ADDR_TOTAL_CURRENCY);
    }

    /**
     * Returns the address of the EdVerifyContract contract.
     *
     * @return the contract address
     */
    public static AionAddress getEdVerifyContractAddress() {
        return AionAddress.wrap(ADDR_ED_VERIFY);
    }

    /**
     * Returns the address of the TxHash contract.
     *
     * @return the contract address
     */
    public static AionAddress getTxHashContractAddress() {
        return AionAddress.wrap(ADDR_TX_HASH);
    }

    /**
     * Returns the address of the blake2b hash contract.
     *
     * @return the contract address
     */
    public static AionAddress getBlake2bHashContractAddress() {
        return AionAddress.wrap(ADDR_BLAKE2B_HASH);
    }
}