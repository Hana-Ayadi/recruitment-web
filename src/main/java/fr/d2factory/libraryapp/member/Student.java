package fr.d2factory.libraryapp.member;

import java.util.Calendar;
import java.util.TimeZone;

import fr.d2factory.libraryapp.member.exception.NoMoneyException;

public class Student extends Member {

	private int yearOfInscription;
	private static final float PRICE = 0.1f;
	private static final float PINALITY_PRICE = 0.15f;
	private static final int PERIOD = 30;
	private static final int TRIAl_PERIOD = 15;
	private Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));

	public Student(float walletMoney, int year) {

		super(walletMoney, PERIOD);
		this.yearOfInscription = year;

	}

	public int getYearOfInscription() {
		return yearOfInscription;
	}

	public void setYearOfInscription(int yearOfInscription) {
		this.yearOfInscription = yearOfInscription;
	}

	@Override
	public void payBook(int numberOfDays) {

		float total = 0;
		if (cal.get(Calendar.YEAR) == yearOfInscription) {

			if (numberOfDays >= TRIAl_PERIOD && numberOfDays <= PERIOD) {

				total = (numberOfDays - TRIAl_PERIOD) * PRICE;

			} else if (numberOfDays > PERIOD) {

				total = TRIAl_PERIOD * PRICE + (numberOfDays - PERIOD) * PINALITY_PRICE;
			}

		} else {
			if (numberOfDays <= PERIOD) {

				total = numberOfDays * PRICE;

			} else {

				total = PERIOD * PRICE + (numberOfDays - PERIOD) * PINALITY_PRICE;

			}
		}

		if (getWallet() >= total) {

			setWallet(getWallet() - total);

		} else {

			throw new NoMoneyException("This member's wallet is empty he has only " + getWallet());
		}

	}

}
