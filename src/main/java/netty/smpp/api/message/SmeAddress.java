package netty.smpp.api.message;

/**
 * The address of an SME.
 *
 * @deprecated Use {@link netty.smpp.api.Address}.
 */
public class SmeAddress extends netty.smpp.api.Address {
    static final long serialVersionUID = -7812497043813226780L;

    public SmeAddress() {
    }

    public SmeAddress(int ton, int npi, String addr) {
        super(ton, npi, addr);
    }
}

