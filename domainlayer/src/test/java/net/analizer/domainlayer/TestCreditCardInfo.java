package net.analizer.domainlayer;

import net.analizer.domainlayer.models.CreditCartInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TestCreditCardInfo {

    @Test
    public void testValidCreditCard() throws Exception {
        CreditCartInfo creditCartInfo = new CreditCartInfo();
        creditCartInfo.setCreditCardHolderName("PANATCHAI VATHANASRI");
        creditCartInfo.setCreditCardNo("38056789000000000");
        creditCartInfo.setCreditCardExpiry("01/22");
        creditCartInfo.setCreditCardCVV("552");

        assertThat(creditCartInfo.isValid(), is(equalTo(true)));
    }

    @Test
    public void testInValidCreditCardNo() throws Exception {
        CreditCartInfo creditCartInfo = new CreditCartInfo();
        creditCartInfo.setCreditCardHolderName("PANATCHAI VATHANASRI");
        creditCartInfo.setCreditCardNo("");
        creditCartInfo.setCreditCardExpiry("01/22");
        creditCartInfo.setCreditCardCVV("552");

        assertThat(creditCartInfo.isValid(), is(equalTo(false)));
    }

    @Test
    public void testInValidExpiry() throws Exception {
        CreditCartInfo creditCartInfo = new CreditCartInfo();
        creditCartInfo.setCreditCardHolderName("PANATCHAI VATHANASRI");
        creditCartInfo.setCreditCardNo("38056789000000000");
        creditCartInfo.setCreditCardExpiry("0122");
        creditCartInfo.setCreditCardCVV("552");

        assertThat(creditCartInfo.isValid(), is(equalTo(false)));
    }

    @Test
    public void testInValidCCV() throws Exception {
        CreditCartInfo creditCartInfo = new CreditCartInfo();
        creditCartInfo.setCreditCardHolderName("PANATCHAI VATHANASRI");
        creditCartInfo.setCreditCardNo("38056789000000000");
        creditCartInfo.setCreditCardExpiry("01/22");
        creditCartInfo.setCreditCardCVV("AAA");

        assertThat(creditCartInfo.isValid(), is(equalTo(false)));
    }
}