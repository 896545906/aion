package org.aion.precompiled.contracts.ATB;

import static com.google.common.truth.Truth.assertThat;
import static org.aion.precompiled.contracts.ATB.BridgeTestUtils.dummyContext;

import java.util.Properties;
import org.aion.types.AionAddress;
import org.aion.db.impl.DBVendor;
import org.aion.db.impl.DatabaseFactory;
import org.aion.interfaces.db.ContractDetails;
import org.aion.interfaces.db.PruneConfig;
import org.aion.interfaces.db.RepositoryCache;
import org.aion.interfaces.db.RepositoryConfig;
import org.aion.mcf.config.CfgPrune;
import org.aion.crypto.HashUtil;

import org.aion.zero.impl.db.AionRepositoryCache;
import org.aion.zero.impl.db.AionRepositoryImpl;
import org.aion.zero.impl.db.ContractDetailsAion;
import org.junit.Before;
import org.junit.Test;

public class BridgeRingInitializationTest {

    private BridgeStorageConnector connector;
    private BridgeController controller;
    private static final AionAddress CONTRACT_ADDR =
            new AionAddress(HashUtil.h256("contractAddress".getBytes()));
    private static final AionAddress OWNER_ADDR =
            new AionAddress(HashUtil.h256("ownerAddress".getBytes()));

    @Before
    public void beforeEach() {
        RepositoryConfig repoConfig =
                new RepositoryConfig() {
                    @Override
                    public String getDbPath() {
                        return "";
                    }

                    @Override
                    public PruneConfig getPruneConfig() {
                        return new CfgPrune(false);
                    }

                    @Override
                    public ContractDetails contractDetailsImpl() {
                        return ContractDetailsAion.createForTesting(0, 1000000).getDetails();
                    }

                    @Override
                    public Properties getDatabaseConfig(String db_name) {
                        Properties props = new Properties();
                        props.setProperty(DatabaseFactory.Props.DB_TYPE, DBVendor.MOCKDB.toValue());
                        props.setProperty(DatabaseFactory.Props.ENABLE_HEAP_CACHE, "false");
                        return props;
                    }
                };
        RepositoryCache repo =
                new AionRepositoryCache(AionRepositoryImpl.createForTesting(repoConfig));
        this.connector = new BridgeStorageConnector(repo, CONTRACT_ADDR);
        this.controller =
                new BridgeController(
                        connector, dummyContext().sideEffects, CONTRACT_ADDR, OWNER_ADDR);
        this.controller.initialize();
    }

    @Test
    public void testRingEmptyInitialization() {
        ErrCode code = this.controller.ringInitialize(OWNER_ADDR.toByteArray(), new byte[][] {});
        assertThat(code).isEqualTo(ErrCode.NO_ERROR);
        assertThat(this.connector.getMemberCount()).isEqualTo(0);
        assertThat(this.connector.getMinThresh()).isEqualTo(1);
    }

    @Test
    public void testRingSingleMemberInitialization() {
        ErrCode code =
                this.controller.ringInitialize(
                        OWNER_ADDR.toByteArray(), new byte[][] {OWNER_ADDR.toByteArray()});
        assertThat(code).isEqualTo(ErrCode.NO_ERROR);
        assertThat(this.connector.getMemberCount()).isEqualTo(1);
        assertThat(this.connector.getMinThresh()).isEqualTo(1);
    }

    @Test
    public void testRingMultiMemberInitialization() {
        byte[][] members =
                new byte[][] {
                    HashUtil.h256("member1".getBytes()),
                    HashUtil.h256("member2".getBytes()),
                    HashUtil.h256("member3".getBytes()),
                    HashUtil.h256("member4".getBytes()),
                    HashUtil.h256("member5".getBytes())
                };
        ErrCode code = this.controller.ringInitialize(OWNER_ADDR.toByteArray(), members);
        assertThat(code).isEqualTo(ErrCode.NO_ERROR);
        assertThat(this.connector.getMemberCount()).isEqualTo(5);
        assertThat(this.connector.getMinThresh()).isEqualTo(3);
    }

    @Test
    public void testRingInitializationNotOwner() {
        byte[] notOwner = HashUtil.h256("not owner".getBytes());
        ErrCode code = this.controller.ringInitialize(notOwner, new byte[][] {});
        assertThat(code).isEqualTo(ErrCode.NOT_OWNER);

        assertThat(this.connector.getMemberCount()).isEqualTo(0);
        assertThat(this.connector.getMinThresh()).isEqualTo(0);
    }
}
