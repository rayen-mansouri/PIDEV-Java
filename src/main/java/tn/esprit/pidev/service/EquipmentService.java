package tn.esprit.pidev.service;

import tn.esprit.pidev.config.Database;
import tn.esprit.pidev.model.Equipment;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EquipmentService {
    public List<Equipment> findAll() throws SQLException {
        String sql = "SELECT ID, NAME, TYPE, STATUS, PURCHASE_DATE FROM EQUIPMENTS ORDER BY ID";
        List<Equipment> equipmentList = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                equipmentList.add(mapRow(resultSet));
            }
        }

        return equipmentList;
    }

    public Optional<Equipment> findById(int id) throws SQLException {
        String sql = "SELECT ID, NAME, TYPE, STATUS, PURCHASE_DATE FROM EQUIPMENTS WHERE ID = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    public void create(Equipment equipment) throws SQLException {
        String sql = "INSERT INTO EQUIPMENTS (ID, NAME, TYPE, STATUS, PURCHASE_DATE) "
            + "VALUES (EQUIPMENT_SEQ.NEXTVAL, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            bindFields(statement, equipment, false);
            statement.executeUpdate();
        }
    }

    public void update(Equipment equipment) throws SQLException {
        String sql = "UPDATE EQUIPMENTS SET NAME = ?, TYPE = ?, STATUS = ?, PURCHASE_DATE = ? WHERE ID = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            bindFields(statement, equipment, true);
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM EQUIPMENTS WHERE ID = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Equipment mapRow(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID");
        String name = resultSet.getString("NAME");
        String type = resultSet.getString("TYPE");
        String status = resultSet.getString("STATUS");
        Date purchaseDate = resultSet.getDate("PURCHASE_DATE");
        LocalDate purchaseLocalDate = purchaseDate != null ? purchaseDate.toLocalDate() : null;
        return new Equipment(id, name, type, status, purchaseLocalDate);
    }

    private void bindFields(PreparedStatement statement, Equipment equipment, boolean includeId) throws SQLException {
        statement.setString(1, equipment.getName());
        statement.setString(2, equipment.getType());
        statement.setString(3, equipment.getStatus());
        if (equipment.getPurchaseDate() != null) {
            statement.setDate(4, Date.valueOf(equipment.getPurchaseDate()));
        } else {
            statement.setNull(4, java.sql.Types.DATE);
        }
        if (includeId) {
            statement.setInt(5, equipment.getId());
        }
    }
}
