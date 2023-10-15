<?php
    require_once 'characters_api.php';

    try {
        $api = new CharactersApi();
        echo $api->run();
    } catch (Exception $e) {
        echo json_encode(Array('error' => $e->getMessage()));
    }
?>