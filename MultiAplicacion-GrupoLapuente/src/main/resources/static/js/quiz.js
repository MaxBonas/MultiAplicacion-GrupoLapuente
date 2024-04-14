document.addEventListener('DOMContentLoaded', function() {
    let currentSlide = 0;
    const slides = document.querySelectorAll('.quiz-slide');
    const quizForm = document.getElementById('quizForm');
    const sociedadId = quizForm.dataset.sociedadid;

    slides.forEach(slide => {
        let bg = slide.getAttribute('data-background');
        slide.style.backgroundImage = `url('${bg}')`;
    });

    document.querySelectorAll('.yes').forEach(button => {
        button.addEventListener('click', () => nextSlide());
    });

    document.querySelectorAll('.no').forEach(button => {
        button.addEventListener('click', () => {
            // Cambio: Asegurar que se envíe el formulario independientemente de la respuesta.
            quizForm.action = `/worker/${sociedadId}/quiz/process`;
            quizForm.submit();
        });
    });

    function showSlide(n) {
        slides.forEach((slide, index) => {
            slide.style.display = index === n ? 'block' : 'none';
        });
    }

    function nextSlide() {
        currentSlide += 1;
        if (currentSlide < slides.length) {
            showSlide(currentSlide);
        } else {
            quizForm.action = `/worker/${sociedadId}/quiz/process`;
            quizForm.submit();
        }
    }

    showSlide(currentSlide);
});
