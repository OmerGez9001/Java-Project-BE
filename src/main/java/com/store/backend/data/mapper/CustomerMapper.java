package com.store.backend.data.mapper;

import com.store.backend.data.dto.CustomerDto;
import com.store.backend.data.model.customer.AbstractCustomer;
import com.store.backend.data.model.customer.NewCustomer;
import com.store.backend.data.model.customer.RepeatCustomer;
import com.store.backend.data.model.customer.VipCustomer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto customerToCustomerDto(AbstractCustomer abstractCustomer);

    NewCustomer toNewCustomer(CustomerDto abstractCustomer);

    VipCustomer toVipCustomer(CustomerDto abstractCustomer);

    RepeatCustomer toRepeatCustomer(CustomerDto abstractCustomer);

    default AbstractCustomer customerDtoToCustomer(CustomerDto customerDto) {
        if (customerDto.getType().equals(NewCustomer.class.getSimpleName())) {
            return toNewCustomer(customerDto);
        } else if (customerDto.getType().equals(VipCustomer.class.getSimpleName())) {
            return toVipCustomer(customerDto);
        } else if (customerDto.getType().equals(RepeatCustomer.class.getSimpleName())) {
            return toRepeatCustomer(customerDto);
        }
        return null;
    }
}
