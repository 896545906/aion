package org.aion.zero.impl.types;

import java.util.List;
import org.aion.mcf.blockchain.Block;

/** @author jay */
public interface BlockSummary {
    List<?> getReceipts();

    Block getBlock();
}
