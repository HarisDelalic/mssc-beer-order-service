package com.dela.msscbeerorderservice.domain;

/**
 * We don't have ALLOCATE event since VALIDATION_PASSED automatically executes allocation event in background,
 * (so ALLOCATE event is as part of VALIDATION_PASSED event)
 * and VALIDATION_PASSED responses with ALLOCATION_SUCCESS, ALLOCATION_NO_INVENTORY, ALLOCATION_FAILED
 */
public enum BeerOrderEvent {
    VALIDATE_ORDER,
    VALIDATION_PASSED,
    VALIDATION_FAILED,
    ALLOCATE_ORDER,
    ALLOCATION_SUCCESS,
    ALLOCATION_NO_INVENTORY,
    ALLOCATION_FAILED,
    PICK_UP,
    CANCEL_ORDER
}
