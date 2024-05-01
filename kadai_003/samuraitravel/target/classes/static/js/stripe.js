const stripe = Stripe('pk_test_51P4WLNP0nsLMqosbS0JSYLcRHhT6TmVqVgK9pLRGJOCJhtXNOiN8efw76a2k1jrpNsC1jcD7cCKFOZ6HrGLGe7wp009Ey24jbg');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
  stripe.redirectToCheckout({
    sessionId: sessionId
  })
});
