class VehicleFeatureTile {
    constructor(private element: HTMLElement) {
        this.init();
    }

    private init() {
        if (!('IntersectionObserver' in window)) {
            return;
        }

        const analyticsId = this.element.dataset.analyticsId;
        if (!analyticsId) return;

        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting && entry.intersectionRatio >= 0.5) {
                    const event = new CustomEvent('tile:viewed', {
                        detail: { id: analyticsId }
                    });

                    this.element.dispatchEvent(event);
                    observer.disconnect();
                }
            });
        }, { threshold: 0.5 });

        observer.observe(this.element);
    }
}

document.querySelectorAll('.feature-tile')
    .forEach(el => new VehicleFeatureTile(el as HTMLElement));