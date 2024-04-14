function updateLogo() {
    const logo = document.getElementById('logo');
    const url = window.location.href;
    const splitUrl = url.split('/');
    const sociedadId = splitUrl[4];
    const role = splitUrl[3];  // 'admin' or 'worker'

    if (sociedadId === '1') {
        logo.src = '/images/GOODPOLISH.png';
    } else if (sociedadId === '2') {
        logo.src = '/images/LAPUENTE.png';
    } else if (sociedadId === '3') {
        logo.src = '/images/SPB.png';
    } else if (sociedadId === '4') {
        logo.src = '/images/ISOTUBI.png';
    } else {
        logo.src = '/images/GL horizontal.png';
    }

    logo.onclick = function() {
        if (role === 'admin') {
            window.location.href = `/admin/${sociedadId}/adminsmenu`;
        } else if (role === 'worker') {
            window.location.href = `/worker/${sociedadId}/workersmenu`;
        }
    }
}
