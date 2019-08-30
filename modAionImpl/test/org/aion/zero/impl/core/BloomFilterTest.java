package org.aion.zero.impl.core;

import static com.google.common.truth.Truth.assertThat;

import java.math.BigInteger;
import org.aion.crypto.HashUtil;
import org.aion.mcf.vm.types.Bloom;
import org.aion.types.AionAddress;
import org.aion.util.types.AddressUtils;
import org.aion.zero.impl.blockchain.BlockchainIntegrationTest;
import org.junit.Test;

/**
 * Very basic bloom filter tests, for integration tests, eventually look for them in {@link
 * BlockchainIntegrationTest}
 *
 * <p>TODO: implement integration tests
 */
public class BloomFilterTest {
    @Test
    public void testSimpleAddSearchBloom() {
        String input = "hello world";
        Bloom bloom = BloomFilter.create(input.getBytes());
        assertThat(BloomFilter.containsString(bloom, input)).isTrue();
    }

    @Test
    public void testContainsAddress() {
        AionAddress addr =
                AddressUtils.wrapAddress(
                        "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
        Bloom bloom = BloomFilter.create(addr.toByteArray());
        assertThat(BloomFilter.containsAddress(bloom, addr)).isTrue();
    }

    @Test
    public void testContainsEvent() {
        byte[] someEvent = HashUtil.h256(BigInteger.TEN.toByteArray());
        Bloom bloom = BloomFilter.create(someEvent);
        assertThat(BloomFilter.containsEvent(bloom, someEvent)).isTrue();
    }

    @Test
    public void testContainsEvent2() {
        String evt = "Created(uint128,address)";
        byte[] someEvent = HashUtil.h256(evt.getBytes());
        Bloom bloom = BloomFilter.create(someEvent);
        assertThat(BloomFilter.containsEvent(bloom, someEvent)).isTrue();
    }

    @Test
    public void testCompositeBloomFiltering() {
        AionAddress addr =
                AddressUtils.wrapAddress(
                        "BEEBEEBEEBEEBEEBEEBEEBEEBEEBEEBEEBEEBEEBEEBEEBEEBEEBEEBEEBEEFFFF");
        byte[] someEvent = HashUtil.h256(BigInteger.ONE.toByteArray());
        byte[] anotherEvent = HashUtil.h256(BigInteger.TWO.toByteArray());

        Bloom bloom = BloomFilter.create(addr.toByteArray(), someEvent, anotherEvent);
        assertThat(BloomFilter.containsAddress(bloom, addr)).isTrue();
        assertThat(BloomFilter.containsEvent(bloom, someEvent)).isTrue();

        // test filtering composite
        Bloom compositeTargetBloom = BloomFilter.create(someEvent, anotherEvent);
        assertThat(BloomFilter.contains(bloom, compositeTargetBloom)).isTrue();
    }
}
