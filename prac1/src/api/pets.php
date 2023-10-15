<?php
    require_once 'pets_api.php';
 
    try {
        $api = new PetsApi();
        echo $api->run();
    } catch (Exception $e) {
        echo json_encode(Array('error' => $e->getMessage()));
    }
?>