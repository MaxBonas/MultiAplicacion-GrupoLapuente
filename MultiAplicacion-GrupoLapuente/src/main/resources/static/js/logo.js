function updateLogo() {
    const logo = document.getElementById('logo');
    const url = window.location.href;
    const sociedadId = url.split('/')[4];

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
}
