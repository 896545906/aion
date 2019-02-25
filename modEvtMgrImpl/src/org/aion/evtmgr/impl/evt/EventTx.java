package org.aion.evtmgr.impl.evt;

import org.aion.evtmgr.IEvent;
import org.aion.evtmgr.impl.abs.AbstractEvent;

/** @author jay */
public class EventTx extends AbstractEvent implements IEvent {

    private int state = -1;
    private int callback = -1;

    public static final int EVTTYPE = TYPE.TX0.getValue();

    public enum STATE {
        /**
         * Transaction may be dropped due to: - Invalid transaction (invalid nonce, low nrg price,
         * insufficient account funds, invalid signature) - Timeout (when pending transaction is not
         * included to any block for last [transaction.outdated.threshold] blocks This is the final
         * state
         */
        DROPPED0(0),
        /**
         * The same as PENDING when transaction is just arrived Next state can be either PENDING or
         * INCLUDED
         */
        NEW_PENDING0(1),
        /**
         * State when transaction is not included to any blocks (on the main chain), and was
         * executed on the last best block. The repository state is reflected in the PendingState
         * Next state can be either INCLUDED, DROPPED (due to timeout) or again PENDING when a new
         * block (without this transaction) arrives
         */
        PENDING0(2),
        /**
         * State when the transaction is included to a block. This could be the final state, however
         * next state could also be PENDING: when a fork became the main chain but doesn't include
         * this tx INCLUDED: when a fork became the main chain and tx is included into another block
         * from the new main chain DROPPED: If switched to a new (long enough) main chain without
         * this Tx
         */
        INCLUDED(3);

        static final int MAX = 3;
        static final int MIN = 0;
        private int value;

        private static final STATE[] intMapState = new STATE[MAX + 1];

        static {
            for (STATE type : STATE.values()) {
                intMapState[0xff & type.value] = type;
            }
        }

        STATE(final int _value) {
            this.value = _value;
        }

        public int getValue() {
            return this.value;
        }

        public static STATE GETSTATE(final int _ctrlInt) {
            if (_ctrlInt < MIN || _ctrlInt > MAX) return null;
            else return intMapState[0xff & _ctrlInt];
        }

        public boolean isPending() {
            return this.value == 1 || this.value == 2;
        }
    }

    public enum CALLBACK {
        PENDINGTXSTATECHANGE0(0),
        PENDINGTXUPDATE0(1),
        PENDINGTXRECEIVED0(2),
        TXEXECUTED0(3),
        TXBACKUP0(4);

        static final int MAX = 127;
        static final int MIN = 0;
        private int value;

        private static final CALLBACK[] intMapCallback = new CALLBACK[MAX + 1];

        static {
            for (CALLBACK type : CALLBACK.values()) {
                intMapCallback[0xff & type.value] = type;
            }
        }

        CALLBACK(final int _value) {
            this.value = _value;
        }

        public int getValue() {
            return this.value;
        }

        public static CALLBACK GETCALLBACK(final int _ctrlInt) {
            if (_ctrlInt < MIN || _ctrlInt > MAX) return null;
            else return intMapCallback[0xff & _ctrlInt];
        }
    }

    public EventTx(CALLBACK _cb) {
        this.callback = _cb.getValue();
    }

    public EventTx(STATE _s, CALLBACK _cb) {
        this.state = _s.getValue();
        this.callback = _cb.getValue();
    }

    public int getEventType() {
        return EventTx.EVTTYPE;
    }

    public int getState() {
        return this.state;
    }

    public int getCallbackType() {
        return this.callback;
    }
}
