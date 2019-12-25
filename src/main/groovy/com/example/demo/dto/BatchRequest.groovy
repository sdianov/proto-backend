package com.example.demo.dto


class BatchItem {

    String operation;
    Object item;
}

class BatchRequest {

    List<BatchItem> batchItems;
}
