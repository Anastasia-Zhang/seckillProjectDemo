package com.miaoshaProject.dao;

import com.miaoshaProject.dataobject.OrderAddressStatusDO;

public interface OrderAddressStatusDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_address_status
     *
     * @mbg.generated Thu May 02 21:52:27 CST 2019
     */
    int deleteByPrimaryKey(Integer id);
    int deleteByOrderId(String orderId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_address_status
     *
     * @mbg.generated Thu May 02 21:52:27 CST 2019
     */
    int insert(OrderAddressStatusDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_address_status
     *
     * @mbg.generated Thu May 02 21:52:27 CST 2019
     */
    int insertSelective(OrderAddressStatusDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_address_status
     *
     * @mbg.generated Thu May 02 21:52:27 CST 2019
     */
    OrderAddressStatusDO selectByPrimaryKey(Integer id);
    OrderAddressStatusDO selectByOrderId(String orderId);


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_address_status
     *
     * @mbg.generated Thu May 02 21:52:27 CST 2019
     */
    int updateByPrimaryKeySelective(OrderAddressStatusDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_address_status
     *
     * @mbg.generated Thu May 02 21:52:27 CST 2019
     */
    int updateByPrimaryKey(OrderAddressStatusDO record);
}