package lab5.domain;

import lab5.application.Repository;

import java.util.Objects;
import java.util.Random;

/**
 * A player.
 */
public class Player {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String postalCode;
    private final String province;
    private final String phoneNumber;

    public Player(int playerId, String firstName, String lastName, String address, String postalCode, String province, String phoneNumber) {
        if (
            firstName == null ||
                lastName == null ||
                address == null ||
                postalCode == null ||
                province == null ||
                phoneNumber == null
        ) {
            throw new IllegalArgumentException("Player constructor arguments cannot be null");
        }
        this.id = playerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postalCode = postalCode;
        this.province = province;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getProvince() {
        return province;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Player{" +
            "id='" + id + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", address='" + address + '\'' +
            ", postalCode='" + postalCode + '\'' +
            ", province='" + province + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id && Objects.equals(firstName, player.firstName) && Objects.equals(lastName, player.lastName) && Objects.equals(address, player.address) && Objects.equals(postalCode, player.postalCode) && Objects.equals(province, player.province) && Objects.equals(phoneNumber, player.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, address, postalCode, province, phoneNumber);
    }

    private static final Random random = new Random(System.currentTimeMillis());

    private static String generatePhoneNumber() {
        return String.format("%03d-%03d-%04d", random.nextInt(1000), random.nextInt(1000), random.nextInt(10000));
    }

    public static Player[] generatePlayers(Repository repository) {
        return generatePlayers(repository, 20);
    }

    public static Player[] generatePlayers(Repository repository, int count) {
        String[] firstNames = {"John", "Emma", "Olivia", "James", "William", "Sophia", "Ava", "Isabella", "Mia", "Charlotte"};
        String[] lastNames = {"Smith", "Johnson", "Brown", "Williams", "Jones", "Miller", "Davis", "Garcia", "Rodriguez", "Wilson"};
        String[] addresses = {"123 Main St", "456 Elm St", "789 Oak St", "101 Maple Ave", "202 Pine St", "303 Birch St", "404 Cedar Ave", "505 Cherry St", "606 Aspen St", "707 Redwood Ave"};
        String[] postalCodes = {"12345", "23456", "34567", "45678", "56789", "67890", "78901", "89012", "90123", "01234"};
        String[] provinces = {"Ontario", "Quebec", "British Columbia", "Alberta", "Manitoba", "Saskatchewan", "Nova Scotia", "New Brunswick", "Newfoundland", "Prince Edward Island"};
        Player[] players = new Player[count];
        for (int i = 0; i < count; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String address = addresses[random.nextInt(addresses.length)] + ", Apt " + (i + 1);
            String postalCode = postalCodes[random.nextInt(postalCodes.length)];
            String province = provinces[random.nextInt(provinces.length)];
            String phoneNumber = generatePhoneNumber();
            players[i] = repository.createPlayer(firstName, lastName, address, postalCode, province, phoneNumber);
        }
        return players;
    }
}
