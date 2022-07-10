// Setup:
//  1) visit https://apilayer.com/ and create an account
//  2) choose the free subscription (250 requests / month)
//  3) go to Account under the top-right bubble to see your API key
//  4) copy paste the API key between the quotation marks on line 10
//      -> its marked with a comment!!!
//  5) Open the HTML file in your browser and check current currency conversions!

document.addEventListener('DOMContentLoaded', () => {
    const KEY = '';                                 /*  input API Key on this line   */
    const from = document.querySelector('#from');
    const to = document.querySelector('#to');

    // populate options
    fetch(`https://api.apilayer.com/exchangerates_data/latest?apikey=${KEY}`)
    .then(response => response.json())
    .then(data => {
        Object.keys(data.rates).forEach(currency => {
            from.add(new Option(currency, currency));
            to.add(new Option(currency, currency));
        });
    })
    .catch(error => console.log('error:', error));
    
    // request currency conversion from exchangerates.io api
    document.querySelector('form').onsubmit = () => {
        fetch(`https://api.apilayer.com/exchangerates_data/latest?apikey=${KEY}&base=${from.value}`)
        .then(response => response.json())
        .then(data => {
            const rate = data.rates[to.value];
            document.querySelector('#result').innerHTML = 
                `1 ${from.value} is equivalent to ${rate.toFixed(3)} ${to.value}.`;
        })
        .catch(error => console.log('error', error));
        return false;
    }
});