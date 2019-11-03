package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.member.exception.NoMoneyException;

public class Resident extends Member {

	private static final float PRICE = 0.1f;
	private static final float PINALITY_PRICE = 0.2f;
	private static final int PERIOD = 60;

	public Resident(float wallet) {
		super(wallet, PERIOD);

	}

	@Override
	public void payBook(int numberOfDays) {
		float total = 0;
		if (numberOfDays <= PERIOD) {

			total = numberOfDays * PRICE;

		} else {

			total = PERIOD * PRICE + (numberOfDays - PERIOD) * PINALITY_PRICE;
		}
		if (getWallet() >= total) {

			setWallet(getWallet() - total);

		} else {

			throw new NoMoneyException("This member's wallet is empty he has only " + getWallet());
		}

	}

}
