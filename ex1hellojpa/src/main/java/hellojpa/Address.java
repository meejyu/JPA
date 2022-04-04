package hellojpa;

import javax.persistence.Embeddable;
import java.util.Objects;

/**
 * 값 타입을 정의하는 곳에 표시
 */
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // 생성자 필수
    public Address() {
    }

    // 기본생성자
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * equals의 재정의의 예
     * 값 타입의 equals() 메소드를 적절하게 재정의(주로 모든 필드 사용)
     * a.equals(b)를 사용해서 동등성 비교를 해야 함.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) &&
                Objects.equals(getStreet(), address.getStreet()) &&
                Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }
}
