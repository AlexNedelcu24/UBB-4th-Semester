<?php
header("Access-Control-Allow-Origin: *");

$mysqli = new mysqli("localhost", "root", "", "lab6");

if ($mysqli->connect_error) {
    exit('Could not connect');
}

if (isset($_GET['departure'])) {
    $departure = $_GET['departure'];

    $sql = "SELECT arrival FROM routes WHERE departure = ?;";

    $stmt = $mysqli->prepare($sql);
    $stmt->bind_param("s", $departure);
    $stmt->execute();
    $stmt->bind_result($arrival);

    echo "<thead><tr><th>Arrivals</th></tr></thead>";
    echo "<tbody style='text-align: center;'>";

    while ($stmt->fetch()) {
        echo "<tr><td>" . $arrival . "</td></tr>";
    }

    echo "</tbody>";

    $stmt->close();
}

$mysqli->close();
?>
