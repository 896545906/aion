package org.aion.precompiled.contracts.ATB;

import static com.google.common.truth.Truth.assertThat;
import static org.aion.precompiled.contracts.ATB.BridgeTestUtils.dummyContext;

import org.aion.crypto.ECKey;
import org.aion.crypto.ECKeyFac;
import org.aion.crypto.HashUtil;
import org.aion.precompiled.ExternalCapabilitiesForTesting;
import org.aion.precompiled.ExternalStateForTests;
import org.aion.precompiled.type.CapabilitiesProvider;
import org.aion.precompiled.type.IExternalStateForPrecompiled;
import org.aion.types.AionAddress;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BridgeControllerRingTest {

    private BridgeStorageConnector connector;
    private BridgeController controller;
    private static final AionAddress CONTRACT_ADDR =
            new AionAddress(HashUtil.h256("contractAddress".getBytes()));
    private static final AionAddress OWNER_ADDR =
            new AionAddress(HashUtil.h256("ownerAddress".getBytes()));

    private static final ECKey members[] =
            new ECKey[] {
                ECKeyFac.inst().create(),
                ECKeyFac.inst().create(),
                ECKeyFac.inst().create(),
                ECKeyFac.inst().create(),
                ECKeyFac.inst().create()
            };

    private static byte[][] getMemberAddress(ECKey[] members) {
        byte[][] memberList = new byte[members.length][];
        for (int i = 0; i < members.length; i++) {
            memberList[i] = members[i].getAddress();
        }
        return memberList;
    }

    @BeforeClass
    public static void setupCapabilities() {
        CapabilitiesProvider.installExternalCapabilities(new ExternalCapabilitiesForTesting());
    }

    @AfterClass
    public static void teardownCapabilities() {
        CapabilitiesProvider.removeExternalCapabilities();
    }

    @Before
    public void beforeEach() {
        IExternalStateForPrecompiled worldState = ExternalStateForTests.usingDefaultRepository();
        this.connector = new BridgeStorageConnector(worldState, CONTRACT_ADDR);
        this.controller =
                new BridgeController(
                        connector, dummyContext().getLogs(), CONTRACT_ADDR, OWNER_ADDR);
        this.controller.initialize();

        byte[][] memberList = new byte[members.length][];
        for (int i = 0; i < members.length; i++) {
            memberList[i] = members[i].getAddress();
        }
        // setup initial ring structure
        this.controller.ringInitialize(OWNER_ADDR.toByteArray(), memberList);
    }

    @Test
    public void testRingInitialization() {
        for (ECKey k : members) {
            assertThat(this.connector.getActiveMember(k.getAddress())).isTrue();
        }
    }

    @Test
    public void testRingReinitialization() {
        ErrCode code =
                this.controller.ringInitialize(OWNER_ADDR.toByteArray(), getMemberAddress(members));
        assertThat(code).isEqualTo(ErrCode.RING_LOCKED);
    }

    private static final byte[] memberAddress = HashUtil.h256("memberAddress".getBytes());

    @Test
    public void testRingAddMember() {
        ErrCode code = this.controller.ringAddMember(OWNER_ADDR.toByteArray(), memberAddress);
        assertThat(code).isEqualTo(ErrCode.NO_ERROR);
    }

    @Test
    public void testRingAddMemberNotOwner() {
        ErrCode code = this.controller.ringAddMember(CONTRACT_ADDR.toByteArray(), memberAddress);
        assertThat(code).isEqualTo(ErrCode.NOT_OWNER);
    }

    @Test
    public void testRingAddExistingMember() {
        // add member twice
        this.controller.ringAddMember(OWNER_ADDR.toByteArray(), memberAddress);
        ErrCode code = this.controller.ringAddMember(OWNER_ADDR.toByteArray(), memberAddress);
        assertThat(code).isEqualTo(ErrCode.RING_MEMBER_EXISTS);
    }

    @Test
    public void testRingRemoveMember() {
        ErrCode code;
        code = this.controller.ringAddMember(OWNER_ADDR.toByteArray(), memberAddress);
        assertThat(code).isEqualTo(ErrCode.NO_ERROR);
        assertThat(this.connector.getActiveMember(memberAddress)).isTrue();

        code = this.controller.ringRemoveMember(OWNER_ADDR.toByteArray(), memberAddress);
        assertThat(code).isEqualTo(ErrCode.NO_ERROR);
        assertThat(this.connector.getActiveMember(memberAddress)).isFalse();
    }

    @Test
    public void testRingRemoveMemberNotOwner() {
        ErrCode code = this.controller.ringRemoveMember(CONTRACT_ADDR.toByteArray(), memberAddress);
        assertThat(code).isEqualTo(ErrCode.NOT_OWNER);
    }

    @Test
    public void testRingRemoveNonExistingMember() {
        ErrCode code = this.controller.ringRemoveMember(OWNER_ADDR.toByteArray(), memberAddress);
        assertThat(code).isEqualTo(ErrCode.RING_MEMBER_NOT_EXISTS);
    }
}
