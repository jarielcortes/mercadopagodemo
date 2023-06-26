package com.mercadopago.sample.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.sample.dto.CardPaymentDTO;
import com.mercadopago.sample.dto.PaymentResponseDTO;
import com.mercadopago.sample.exception.MercadoPagoException;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CardPaymentService {

  public PaymentResponseDTO processPayment(CardPaymentDTO cardPaymentDTO) {
    try {
      String mercadoPagoAccessToken = "TEST-8486303170090859-022114-b9cc3e47a50387392220b13676781239-1302162091";
      MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

      PaymentClient paymentClient = new PaymentClient();

      PaymentCreateRequest paymentCreateRequest =
          PaymentCreateRequest.builder()
              .transactionAmount(cardPaymentDTO.getTransactionAmount())
              .token(cardPaymentDTO.getToken())
              .description(cardPaymentDTO.getProductDescription())
              .installments(cardPaymentDTO.getInstallments())
              .paymentMethodId(cardPaymentDTO.getPaymentMethodId())
              .payer(
                  PaymentPayerRequest.builder()
                      .email(cardPaymentDTO.getPayer().getEmail())
                      .identification(
                          IdentificationRequest.builder()
                              .type(cardPaymentDTO.getPayer().getIdentification().getType())
                              .number(cardPaymentDTO.getPayer().getIdentification().getNumber())
                              .build())
                      .build())
              .build();

      Payment createdPayment = paymentClient.create(paymentCreateRequest);

      return new PaymentResponseDTO(
          createdPayment.getId(),
          String.valueOf(createdPayment.getStatus()),
          createdPayment.getStatusDetail(),
          "",
          "");
    } catch (MPApiException apiException) {
      System.out.println(apiException.getApiResponse().getContent());
      throw new MercadoPagoException(apiException.getApiResponse().getContent());
    } catch (MPException exception) {
      System.out.println(exception.getMessage());
      throw new MercadoPagoException(exception.getMessage());
    }
  }

  public PaymentResponseDTO processPayment3DS(CardPaymentDTO cardPaymentDTO) {
    try {
      String mercadoPagoAccessToken = "TEST-8486303170090859-022114-b9cc3e47a50387392220b13676781239-1302162091";
      
      MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);
      
      PaymentClient client = new PaymentClient();
      
      PaymentCreateRequest createRequest =
          PaymentCreateRequest.builder()
              .transactionAmount(cardPaymentDTO.getTransactionAmount())
              .token(cardPaymentDTO.getToken())
              .description(cardPaymentDTO.getProductDescription())
              .installments(cardPaymentDTO.getInstallments())
              .paymentMethodId(cardPaymentDTO.getPaymentMethodId())
              .payer(
                PaymentPayerRequest.builder()
                  .email(cardPaymentDTO.getPayer().getEmail())
                  .build()
              )
              .threeDSecureMode("optional")
              .build();
      
      Payment createdPayment = client.create(createRequest);

      return new PaymentResponseDTO(
          createdPayment.getId(),
          String.valueOf(createdPayment.getStatus()),
          createdPayment.getStatusDetail(), 
          createdPayment.getThreeDSInfo() != null ? createdPayment.getThreeDSInfo().getExternalResourceUrl() : null, 
          createdPayment.getThreeDSInfo() != null ? createdPayment.getThreeDSInfo().getCreq() : null);
    } catch (MPApiException apiException) {
      System.out.println(apiException.getApiResponse().getContent());
      throw new MercadoPagoException(apiException.getApiResponse().getContent());
    } catch (MPException exception) {
      System.out.println(exception.getMessage());
      throw new MercadoPagoException(exception.getMessage());
    }
  }
}
