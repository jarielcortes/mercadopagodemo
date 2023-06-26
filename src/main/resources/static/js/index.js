const publicKey = document.getElementById("mercado-pago-public-key").value;
const mp = new MercadoPago(publicKey);

const cardNumberElement = mp.fields.create('cardNumber', {
    placeholder: "Número de la tarjeta"
  }).mount('form-checkout__cardNumber');
  const expirationDateElement = mp.fields.create('expirationDate', {
    placeholder: "MM/YY",
  }).mount('form-checkout__expirationDate');
  const securityCodeElement = mp.fields.create('securityCode', {
    placeholder: "Código de seguridad"
  }).mount('form-checkout__securityCode');

  const idTypes = mp.getIdentificationTypes({
  });


    const paymentMethodElement = document.getElementById('paymentMethodId');
    const issuerElement = document.getElementById('form-checkout__issuer');
    const installmentsElement = document.getElementById('form-checkout__installments');
    const issuerIdElement = document.getElementById('issuerId');
    const paymentTotalElement = document.getElementById('form-checkout__total')

    const issuerPlaceholder = "Banco emisor";
    const installmentsPlaceholder = "Cuotas";

    let currentBin;
    cardNumberElement.on('binChange', async (data) => {
      const { bin } = data;
      try {
        if (!bin && paymentMethodElement.value) {
          clearSelectsAndSetPlaceholders();
          paymentMethodElement.value = "";
        }

        if (bin && bin !== currentBin) {
          const { results } = await mp.getPaymentMethods({ bin });
          const paymentMethod = results[0];

          paymentMethodElement.value = paymentMethod.id;
          updatePCIFieldsSettings(paymentMethod);
          updateIssuer(paymentMethod, bin);
          //updateInstallments(paymentMethod, bin);
          issuerIdElement.value = paymentMethod.issuer.id;
        }

        currentBin = bin;
      } catch (e) {
        console.error('error getting payment methods: ', e)
      }
    });

    function clearSelectsAndSetPlaceholders() {
      clearHTMLSelectChildrenFrom(issuerElement);
      createSelectElementPlaceholder(issuerElement, issuerPlaceholder);

      clearHTMLSelectChildrenFrom(installmentsElement);
      createSelectElementPlaceholder(installmentsElement, installmentsPlaceholder);
    }

    function clearHTMLSelectChildrenFrom(element) {
      const currOptions = [...element.children];
      currOptions.forEach(child => child.remove());
    }

    function createSelectElementPlaceholder(element, placeholder) {
      const optionElement = document.createElement('option');
      optionElement.textContent = placeholder;
      optionElement.setAttribute('selected', "");
      optionElement.setAttribute('disabled', "");

      element.appendChild(optionElement);
    }

    // Este paso mejora las validaciones de cardNumber y securityCode
    function updatePCIFieldsSettings(paymentMethod) {
      const { settings } = paymentMethod;

      const cardNumberSettings = settings[0].card_number;
      cardNumberElement.update({
        settings: cardNumberSettings
      });

      const securityCodeSettings = settings[0].security_code;
      securityCodeElement.update({
        settings: securityCodeSettings
      });
    }

    async function updateIssuer(paymentMethod, bin) {
        const { additional_info_needed, issuer } = paymentMethod;
        let issuerOptions = [issuer];
  
        if (additional_info_needed.includes('issuer_id')) {
          issuerOptions = await getIssuers(paymentMethod, bin);
        }
  
        //createSelectOptions(issuerElement, issuerOptions);
      }
  
      async function getIssuers(paymentMethod, bin) {
        try {
          const { id: paymentMethodId } = paymentMethod;
          return await mp.getIssuers({ paymentMethodId, bin });
        } catch (e) {
          console.error('error getting issuers: ', e)
        }
      };


      async function updateInstallments(paymentMethod, bin) {
        try {
          const installments = await mp.getInstallments({
            amount: document.getElementById('transactionAmount').value,
            bin,
            paymentTypeId: 'credit_card'
          });
          const installmentOptions = installments[0].payer_costs;
          const installmentOptionsKeys = { label: 'recommended_message', value: 'installments' };
          //createSelectOptions(installmentsElement, installmentOptions, installmentOptionsKeys);
        } catch (error) {
          console.error('error getting installments: ', e)
        }
      }

    const formElement = document.getElementById('form-checkout');
    formElement.addEventListener('submit', createCardToken);

    async function createCardToken(event) {
      try {
        const tokenElement = document.getElementById('token');
        if (true) {
          event.preventDefault();
          const token = await mp.fields.createCardToken({
            cardholderName: document.getElementById('form-checkout__cardholderName').value
          });
          tokenElement.value = token.id;
          //formElement.requestSubmit();

          const tokenMP = token.id;
          const issuerId = issuerIdElement.value;
          const paymentMethodId = paymentMethodElement.value;
          const transactionAmount = paymentTotalElement.value;
          const installments = '1';
          const productDescription = 'Boleto';
          const email = 'jariel.cortes@gmail.com';

          fetch("/process_payment", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                    token: tokenMP,
                    issuerId: issuerId,
                    paymentMethodId: paymentMethodId,
                    transactionAmount: Number(transactionAmount),
                    installments: Number(installments),
                    description: productDescription,
                    payer: {
                        email,
                        identification: {
                            type: '',
                            number: '',
                        },
                    },
                }),
            })
            .then(response => {
                return response.json();
            });
        }
      } catch (e) {
        console.error('error creating card token: ', e)
      }
    }