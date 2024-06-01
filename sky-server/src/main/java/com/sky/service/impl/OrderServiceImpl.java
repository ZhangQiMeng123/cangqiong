package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    /**
     * 用户下订单
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //异常情况的处理（收货地址为空、超出配送范围、购物车为空）
        //根据ordersSubmitDTO中的AddressBookId获取地址，判断地址是否为空
        AddressBook addressBook=addressBookMapper.getAddressById(ordersSubmitDTO.getAddressBookId());
        if(addressBook==null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //判断购物车是否为空
        List<ShoppingCart> shoppingCarts=shoppingCartMapper.getCards();
        if(shoppingCarts==null || shoppingCarts.size()==0){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        //构造订单数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
        orders.setNumber(String.valueOf(System.currentTimeMillis()));  //订单号
        orders.setUserId(BaseContext.getCurrentId());
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setPayStatus(Orders.UN_PAID);
        orders.setOrderTime(LocalDateTime.now());
        //向订单表中插入数据
        orderMapper.saveOrder(orders);

        //订单明细数据
        ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<>();
        for(ShoppingCart shoppingCart:shoppingCarts){
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart,orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailArrayList.add(orderDetail);
        }
        //向明细表中插入n条数据
        orderDetailMapper.insertBatch(orderDetailArrayList);
        //清理购物车中的数据
        shoppingCartMapper.clean(BaseContext.getCurrentId());
        //封装返回结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
        return orderSubmitVO;
    }


    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

//        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
//
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "ORDERPAID");

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        // 替代微信支付成功后的数据库订单状态更新，直接在这里更新了
        // 根据订单号查询当前用户的该订单
        Orders ordersDB = orderMapper.getByNumberAndUserId(ordersPaymentDTO.getOrderNumber(), userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders=new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.TO_BE_CONFIRMED);
        orders.setPayStatus(Orders.PAID);
        orders.setCheckoutTime(LocalDateTime.now());
//        Orders orders = Orders.builder()
//                .id(ordersDB.getId())
//                .status(Orders.TO_BE_CONFIRMED) // 订单状态，待接单
//                .payStatus(Orders.PAID) // 支付状态，已支付
//                .checkoutTime(LocalDateTime.now()) // 更新支付时间
//                .build();

        orderMapper.update(orders);

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单号查询当前用户的订单
        Orders ordersDB = orderMapper.getByNumberAndUserId(outTradeNo, userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * 历史订单查询
     */
    @Override
    public PageResult getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        //开启分页查询
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
        //数据库中查询订单
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        Page<Orders> pageResult=orderMapper.getHistoryOrders(ordersPageQueryDTO);
        Long total=pageResult.getTotal();
        //创建返回给前端订单页面的列表
        List<OrderVO> records=new ArrayList<>();
        if(total>0){
            for(Orders orders: pageResult){
              //获取订单的id，以便下面根据订单的id获得订单详情的数据
                Long orderId=orders.getId();
                List<OrderDetail> orderDetails=orderDetailMapper.getHistoryOrderDetails(orderId);
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders,orderVO);
                orderVO.setOrderDetailList(orderDetails);
                records.add(orderVO);
            }
        }
        return new PageResult(total,records);
    }

    /**
     * 根据id查看订单信息
     * @param id
     * @return
     */
    @Override
    public OrderVO getOrderDetailById(Long id) {
        Orders orders=orderMapper.getOrderById(id);
        //根据订单的id去查看订单详细表中的数据
        List<OrderDetail> orderDetails=orderDetailMapper.getHistoryOrderDetails(id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders,orderVO);
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }

    /**
     * 取消订单
     * @param id
     */
    @Override
    public void deleteByOrderId(Long id) throws Exception {
        //根据前台传过来的id删除orders表中对应的订单
        Orders order = orderMapper.getOrderById(id);
        if(order==null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND); // 不存在该订单
        }
        if(order.getStatus()>2){
            //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR); //只有待付款和待接单可以直接取消
        }
        // 订单处于待接单状态下取消，需要进行退款
        if(order.getStatus().equals(Orders.TO_BE_CONFIRMED)){
            //调用微信支付接口进行退款
//            weChatPayUtil.refund(
//                    order.getNumber(), //商户订单号
//                    order.getNumber(), //商户退款单号
//                    new BigDecimal(0.01),//退款金额，单位 元
//                    new BigDecimal(0.01));//原订单金额
            //将支付状态修改为退款
            order.setPayStatus(Orders.REFUND);
        }
        //更新订单状态、取消原因、取消时间
        order.setStatus(Orders.CANCELLED);
        order.setCancelReason("用户取消");
        order.setCancelTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    /**
     * 再来一单
     * @param id
     */
    @Override
    public void repetitionOne(Long id) {
        //根据当前id查询订单明细表 对应同一个购车内有多个菜品数据
        List<OrderDetail> orderDetails=orderDetailMapper.getHistoryOrderDetails(id);
        Long userId=BaseContext.getCurrentId();
        //将订单详情对象转化为购物车对象
        List<ShoppingCart> shoppingCartList=orderDetails.stream().map(x->{
            ShoppingCart shoppingCart = new ShoppingCart();
            //将订单详情里面的菜品信息重新复制到购物车对象中
            BeanUtils.copyProperties(x,shoppingCart,"id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());
        //将购物车对象批量加入数据库
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * 管理端按条件查询订单详情
     * @param ordersPageQueryDTO
     * @return
     */

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
         PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
         Page<Orders> page=orderMapper.pageQuery(ordersPageQueryDTO);
         //待派送 派送中 已完成的订单需要额外返回订单菜品信息，将Orders转化为OrderVo
         //List<OrderVO> orderVOList=getOrdervoList(page);
         return new PageResult(page.getTotal(),page.getResult());
    }

//    private List<OrderVO> getOrdervoList(Page<Orders> page) {
//        //创建返回OrderVO的列表
//        List<OrderVO> orderVOList=new ArrayList<>();
//        List<Orders> ordersList=page.getResult();
//        if(!CollectionUtils.isEmpty(ordersList)) {
//            for (Orders orders : ordersList) {
//                OrderVO orderVO = new OrderVO();
//                BeanUtils.copyProperties(orders, orderVO);
//                String orderDishes = getOrderDishesStr(orders);
//
//                // 将订单菜品信息封装到orderVO中，并添加到orderVOList
//                orderVO.setOrderDishes(orderDishes);
//                orderVOList.add(orderVO);
//
//            }
//        }
//        return orderVOList;
//    }
//
//    private String getOrderDishesStr(Orders orders) {
//        // 查询订单菜品详情信息（订单中的菜品和数量）
//        List<OrderDetail> orderDetailList = orderDetailMapper.getHistoryOrderDetails(orders.getId());
//
//        // 将每一条订单菜品信息拼接为字符串（格式：宫保鸡丁*3；）
//        List<String> orderDishList = orderDetailList.stream().map(x -> {
//            String orderDish = x.getName() + "*" + x.getNumber() + ";";
//            return orderDish;
//        }).collect(Collectors.toList());
//
//        // 将该订单对应的所有菜品信息拼接在一起
//        return String.join("", orderDishList);
//    }

    /**
     * 统计各个状态的订单数量
     * @return
     */
    @Override
    public OrderStatisticsVO getStatusOrdersCount() {
        //查出所有订单
        List<Orders> ordersList=orderMapper.getOrdersCount();
        //根据订单的状态统计数量
        Integer count_confirmed=0; //待派送数量
        Integer count_deliveryInProgress=0; //派送中数量
        Integer count_toBeConfirmed=0; //待接单数量
        for(Orders orders:ordersList){
            if(orders.getStatus().equals(Orders.CONFIRMED)) count_confirmed++;
            if(orders.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) count_deliveryInProgress++;
            if(orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) count_toBeConfirmed++;
        }
//        log.info("待派送数量:{}",count_confirmed);
//        log.info("派送中数量:{}",count_deliveryInProgress);
//        log.info("待接单数量:{}",count_confirmed);
        //将不同状态的数量进行封装然偶返回
        return new OrderStatisticsVO(count_toBeConfirmed,count_confirmed,count_deliveryInProgress);
    }

    /**
     * 商家接单
     * @param ordersConfirmDTO
     */
    @Override
    public void receiveOrder(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder().id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED).build();
        orderMapper.update(orders);
    }

    /**
     * 商家拒单
     * @param ordersRejectionDTO
     */
    @Override
    public void refuseOrders(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        //根据id查询订单的状态 只有待接单的订单才能拒绝接单
        Orders orders=orderMapper.getOrderById(ordersRejectionDTO.getId());
        if(orders==null || !orders.getStatus().equals(Orders.TO_BE_CONFIRMED)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //支付状态
//        Integer payStatus = orders.getPayStatus();
//        if (payStatus == Orders.PAID) {
//            //用户已支付，需要退款
//            String refund = weChatPayUtil.refund(
//                    orders.getNumber(),
//                    orders.getNumber(),
//                    new BigDecimal(0.01),
//                    new BigDecimal(0.01));
//            log.info("申请退款：{}", refund);
//        }
        //更新订单信息 订单状态 取消原因 取消时间 支付状态
        orders = Orders.builder()
                .id(ordersRejectionDTO.getId())
                .status(Orders.CANCELLED)
                .cancelReason("商家拒绝接单:"+ordersRejectionDTO.getRejectionReason())
                .cancelTime(LocalDateTime.now())
                .payStatus(Orders.REFUND)
                .build();
        orderMapper.update(orders);



    }
    /**
     * 商家取消订单
     * @param ordersCancelDTO
     */
    @Override
    public void cancelOrders(OrdersCancelDTO ordersCancelDTO) {
        Orders orders=orderMapper.getOrderById(ordersCancelDTO.getId());
        if(orders==null || orders.getStatus().equals(Orders.COMPLETED)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //支付状态
//        Integer payStatus = orders.getPayStatus();
//        if (payStatus == Orders.PAID) {
//            //用户已支付，需要退款
//            String refund = weChatPayUtil.refund(
//                    orders.getNumber(),
//                    orders.getNumber(),
//                    new BigDecimal(0.01),
//                    new BigDecimal(0.01));
//            log.info("申请退款：{}", refund);
//        }
        //更新订单信息 订单状态 取消原因 取消时间 支付状态
       orders = Orders.builder()
                .id(ordersCancelDTO.getId())
                .status(Orders.CANCELLED)
                .cancelReason("商家取消接单:"+ordersCancelDTO.getCancelReason())
                .cancelTime(LocalDateTime.now())
                .payStatus(Orders.REFUND)
                .build();
        orderMapper.update(orders);

    }

    /**
     * 派送订单
     * @param id
     */
    @Override
    public void delieveOrder(Long id) {
        Orders orders=orderMapper.getOrderById(id);
        if(orders==null || !orders.getStatus().equals(Orders.CONFIRMED)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
         orders = Orders.builder()
                .id(id)
                .status(Orders.DELIVERY_IN_PROGRESS)
                .build();
        orderMapper.update(orders);
    }

    /**
     * 完成订单
     * @param id
     */
    @Override
    public void completeOrders(Long id) {
        Orders orders=orderMapper.getOrderById(id);
        if(orders==null || !orders.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
         orders = Orders.builder()
                .id(id)
                .status(Orders.COMPLETED)
                .build();
        orderMapper.update(orders);
    }
}
