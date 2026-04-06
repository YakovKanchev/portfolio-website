document.querySelectorAll('.exp-header').forEach(header => {
    const item = header.parentElement;
    const content = item.querySelector('.exp-content');
    const toggle = header.querySelector('.exp-toggle');

    if (item.classList.contains('is-open') && content) {
        content.style.maxHeight = content.scrollHeight + 'px';
        if (toggle) {
            toggle.textContent = '-';
        }
    }

    header.addEventListener('click', () => {
        const content = item.querySelector('.exp-content');
        const isOpen = item.classList.toggle('is-open');

        if (toggle) {
            toggle.textContent = isOpen ? '-' : '+';
        }

        if (!content) {
            return;
        }

        if (isOpen) {
            content.style.maxHeight = content.scrollHeight + 'px';
        } else {
            content.style.maxHeight = content.scrollHeight + 'px';
            window.requestAnimationFrame(() => {
                content.style.maxHeight = '0px';
            });
        }
    });
});

const bulgariaClock = document.querySelector('[data-bulgaria-clock] .clock-time');
const menuToggle = document.querySelector('[data-menu-toggle]');
const mobileNav = document.querySelector('[data-mobile-nav]');

if (menuToggle && mobileNav) {
    menuToggle.addEventListener('click', () => {
        const isOpen = mobileNav.classList.toggle('is-open');
        menuToggle.setAttribute('aria-expanded', String(isOpen));
    });
}

function updateBulgariaClock() {
    if (!bulgariaClock) {
        return;
    }

    const time = new Intl.DateTimeFormat('en-GB', {
        timeZone: 'Europe/Sofia',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
    }).format(new Date());

    bulgariaClock.textContent = time;
}

updateBulgariaClock();
setInterval(updateBulgariaClock, 1000);

const aboutSection = document.querySelector('.reveal-home-about');
const aboutTypingText = document.querySelector('.about .card p');
const deferredHomeSections = document.querySelectorAll('.reveal-after-about');
const aboutIntroSessionKey = 'about-intro-complete';
const deferredPageSections = document.querySelectorAll('.reveal-on-load');

function prefersReducedMotion() {
    return window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;
}

function runAfterInitialPaint(callback) {
    window.requestAnimationFrame(() => {
        window.requestAnimationFrame(callback);
    });
}

function revealPageSections() {
    if (!deferredPageSections.length || !document.body.classList.contains('page-sections-pending')) {
        return;
    }

    if (window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
        document.body.classList.remove('page-sections-pending');
        document.body.classList.add('page-sections-ready');
        return;
    }

    runAfterInitialPaint(() => {
        deferredPageSections.forEach((section, index) => {
            section.style.transitionDelay = `${index * 120}ms`;
        });

        document.body.classList.remove('page-sections-pending');
        document.body.classList.add('page-sections-ready');

        window.setTimeout(() => {
            deferredPageSections.forEach(section => {
                section.style.transitionDelay = '';
            });
        }, deferredPageSections.length * 120 + 500);
    });
}

function revealHomeSections() {
    if (!deferredHomeSections.length) {
        return;
    }

    runAfterInitialPaint(() => {
        deferredHomeSections.forEach((section, index) => {
            section.style.transitionDelay = `${index * 120}ms`;
        });

        document.body.classList.remove('home-intro-pending');
        document.body.classList.add('home-intro-ready');

        window.setTimeout(() => {
            deferredHomeSections.forEach(section => {
                section.style.transitionDelay = '';
            });
        }, deferredHomeSections.length * 120 + 500);
    });
}

function revealAboutSection(callback) {
    if (!aboutSection || document.body.classList.contains('home-about-ready')) {
        if (callback) {
            callback();
        }
        return;
    }

    if (prefersReducedMotion()) {
        document.body.classList.remove('home-about-pending');
        document.body.classList.add('home-about-ready');
        if (callback) {
            callback();
        }
        return;
    }

    runAfterInitialPaint(() => {
        document.body.classList.remove('home-about-pending');
        document.body.classList.add('home-about-ready');

        window.setTimeout(() => {
            if (callback) {
                callback();
            }
        }, 140);
    });
}

function runAboutTyping() {
    if (!aboutTypingText) {
        return;
    }

    if (deferredHomeSections.length) {
        document.body.classList.add('home-intro-pending');
        document.body.classList.remove('home-intro-ready');
    }

    const fullText = aboutTypingText.textContent.replace(/\s+/g, ' ').trim();

    if (!fullText) {
        revealAboutSection(revealHomeSections);
        return;
    }

    if (window.sessionStorage && window.sessionStorage.getItem(aboutIntroSessionKey) === 'done') {
        aboutTypingText.textContent = fullText;
        aboutTypingText.classList.remove('typing-text', 'is-typing');
        revealAboutSection(revealHomeSections);
        return;
    }

    if (prefersReducedMotion()) {
        aboutTypingText.textContent = fullText;
        if (window.sessionStorage) {
            window.sessionStorage.setItem(aboutIntroSessionKey, 'done');
        }
        revealAboutSection(revealHomeSections);
        return;
    }

    revealAboutSection(() => {
        let index = 0;
        aboutTypingText.textContent = '';
        aboutTypingText.classList.add('typing-text', 'is-typing');

        const typeNextCharacter = () => {
            index += 1;
            aboutTypingText.textContent = fullText.slice(0, index);

            if (index < fullText.length) {
                window.setTimeout(typeNextCharacter, 28);
                return;
            }

            aboutTypingText.classList.remove('is-typing');
            if (window.sessionStorage) {
                window.sessionStorage.setItem(aboutIntroSessionKey, 'done');
            }
            revealHomeSections();
        };

        window.setTimeout(typeNextCharacter, 120);
    });
}

runAboutTyping();
revealPageSections();

const codeModal = document.getElementById('code-modal');
const codeModalTitle = document.getElementById('code-modal-title');
const codeModalFilebar = document.getElementById('code-modal-filebar');
const codeModalCode = document.getElementById('code-modal-code');
let codeModalCloseTimeout = null;
let activeCodeRequest = 0;

function escapeHtml(value) {
    return value
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;');
}

function applyTokenPlaceholders(line, patterns) {
    const tokens = [];
    let output = line;

    patterns.forEach(pattern => {
        output = output.replace(pattern.regex, match => {
            const placeholder = String.fromCharCode(0xE000 + tokens.length);
            tokens.push(`<span class="${pattern.className}">${escapeHtml(match)}</span>`);
            return placeholder;
        });
    });

    output = escapeHtml(output);

    tokens.forEach((token, index) => {
        output = output.replace(String.fromCharCode(0xE000 + index), token);
    });

    return output;
}

function highlightJava(line) {
    return applyTokenPlaceholders(line, [
        { regex: /\/\/.*$/g, className: 'code-token-comment' },
        { regex: /"[^"]*"/g, className: 'code-token-string' },
        { regex: /@[A-Za-z_][A-Za-z0-9_]*/g, className: 'code-token-annotation' },
        { regex: /\b(package|import|public|private|protected|class|static|final|return|new|void|int|float|boolean|null|this)\b/g, className: 'code-token-keyword' },
        { regex: /\b(Response|String|Gson|GsonBuilder|RestAssured|ContentType|ItemApi|ItemDTO|LoginApi|LoginDTO|Tags|ArrayList)\b/g, className: 'code-token-type' },
        { regex: /\b([a-zA-Z_][a-zA-Z0-9_]*)\b(?=\()/g, className: 'code-token-method' },
        { regex: /\b\d+(?:\.\d+)?\b/g, className: 'code-token-number' }
    ]);
}

function highlightXml(line) {
    return applyTokenPlaceholders(line, [
        { regex: /<\/?[A-Za-z0-9:._-]+/g, className: 'code-token-xml-tag' },
        { regex: /\b[A-Za-z_:][-A-Za-z0-9_:.]*(?==)/g, className: 'code-token-xml-attr' },
        { regex: /"[^"]*"/g, className: 'code-token-xml-value' }
    ]);
}

function renderHighlightedLine(line, fileName) {
    if (fileName.endsWith('.xml')) {
        return highlightXml(line);
    }

    return highlightJava(line);
}

function showCodeModal(fileName, content) {
    if (!codeModal || !codeModalTitle || !codeModalFilebar || !codeModalCode) {
        return;
    }

    codeModalTitle.textContent = fileName;
    codeModalFilebar.textContent = 'API_Testing_Java_Yakov / ' + fileName;
    const codeLines = content.replace(/\r\n/g, '\n').split('\n');
    codeModalCode.innerHTML = codeLines.map((line, index) => {
        const highlightedCode = renderHighlightedLine(line, fileName);

        return '<div class="code-line"><span class="code-line-number">' +
            String(index + 1) +
            '</span><span class="code-line-text">' +
            (highlightedCode || ' ') +
            '</span></div>';
    }).join('');

    if (codeModalCloseTimeout) {
        clearTimeout(codeModalCloseTimeout);
        codeModalCloseTimeout = null;
    }

    codeModal.classList.remove('is-closing', 'is-visible');
    codeModal.hidden = false;
    document.body.style.overflow = 'hidden';
}

async function openCodeModal(button) {
    const filePath = button.dataset.codePath;
    const targetId = button.dataset.codeTarget;

    if (!codeModal || !codeModalTitle || !codeModalFilebar || !codeModalCode) {
        return;
    }

    const fileName = button.dataset.fileLabel || button.querySelector('.repo-file-name')?.textContent || 'File preview';
    const requestId = ++activeCodeRequest;

    showCodeModal(fileName, '// Loading file...');

    if (!filePath && targetId) {
        const source = document.getElementById(targetId);

        if (source) {
            showCodeModal(fileName, source.textContent);
            return;
        }
    }

    if (!filePath) {
        showCodeModal(fileName, '// File preview is unavailable right now.');
        return;
    }

    try {
        const fileUrl = new URL(filePath, window.location.href);
        const response = await fetch(fileUrl.toString(), { cache: 'no-store' });

        if (!response.ok) {
            throw new Error('Unable to load file');
        }

        const content = await response.text();

        if (requestId !== activeCodeRequest) {
            return;
        }

        showCodeModal(fileName, content);
    } catch (error) {
        if (requestId !== activeCodeRequest) {
            return;
        }

        showCodeModal(fileName, '// File preview is unavailable right now.');
    }
}

function closeCodeModal() {
    if (!codeModal) {
        return;
    }

    if (codeModal.hidden) {
        return;
    }

    if (codeModalCloseTimeout) {
        clearTimeout(codeModalCloseTimeout);
        codeModalCloseTimeout = null;
    }

    codeModal.hidden = true;
    codeModal.classList.remove('is-closing', 'is-visible');
    document.body.style.overflow = '';
}

document.addEventListener('click', event => {
    const button = event.target.closest('[data-code-path], [data-code-target]');

    if (!button) {
        return;
    }

    event.preventDefault();
    openCodeModal(button);
});

document.querySelectorAll('[data-close-code-modal]').forEach(element => {
    element.addEventListener('click', closeCodeModal);
});

document.addEventListener('keydown', event => {
    if (event.key === 'Escape') {
        closeCodeModal();
    }
});

const imageModal = document.getElementById('image-modal');
const imageModalTitle = document.getElementById('image-modal-title');
const imageModalView = document.getElementById('image-modal-view');

function openImageModal(button) {
    if (!imageModal || !imageModalTitle || !imageModalView) {
        return;
    }

    const imagePath = button.dataset.imageModalTarget;
    const imageLabel = button.dataset.imageModalLabel || 'Image preview';

    if (!imagePath) {
        return;
    }

    imageModalTitle.textContent = imageLabel;
    imageModalView.src = imagePath;
    imageModalView.alt = imageLabel;
    imageModal.hidden = false;
    document.body.style.overflow = 'hidden';
}

function closeImageModal() {
    if (!imageModal || imageModal.hidden) {
        return;
    }

    imageModal.hidden = true;
    if (imageModalView) {
        imageModalView.src = '';
        imageModalView.alt = '';
    }
    document.body.style.overflow = '';
}

document.addEventListener('click', event => {
    const imageButton = event.target.closest('[data-image-modal-target]');

    if (imageButton) {
        event.preventDefault();
        openImageModal(imageButton);
        return;
    }
});

document.querySelectorAll('[data-close-image-modal]').forEach(element => {
    element.addEventListener('click', closeImageModal);
});

document.addEventListener('keydown', event => {
    if (event.key === 'Escape') {
        closeImageModal();
    }
});
