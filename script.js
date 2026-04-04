document.querySelectorAll('.exp-header').forEach(header => {
    header.addEventListener('click', () => {
        const item = header.parentElement;
        const toggle = header.querySelector('.exp-toggle');
        const isOpen = item.classList.toggle('is-open');

        toggle.textContent = isOpen ? '-' : '+';
    });
});

const bulgariaClock = document.querySelector('[data-bulgaria-clock] .clock-time');
const themeToggle = document.querySelector('[data-theme-toggle]');
const themeLogos = document.querySelectorAll('[data-theme-logo]');
const savedTheme = localStorage.getItem('portfolio-theme');

if (savedTheme === 'light') {
    document.body.classList.add('light-mode');
}

function updateThemeToggleLabel() {
    if (!themeToggle) {
        return;
    }

    themeToggle.textContent = document.body.classList.contains('light-mode') ? 'Dark' : 'Light';
}

function updateThemeLogos() {
    const isLight = document.body.classList.contains('light-mode');

    themeLogos.forEach(logo => {
        logo.src = isLight ? logo.dataset.lightLogo : logo.dataset.darkLogo;
    });
}

if (themeToggle) {
    themeToggle.addEventListener('click', () => {
        document.body.classList.toggle('light-mode');
        localStorage.setItem(
            'portfolio-theme',
            document.body.classList.contains('light-mode') ? 'light' : 'dark'
        );
        updateThemeToggleLabel();
        updateThemeLogos();
    });
}

updateThemeToggleLabel();
updateThemeLogos();

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

const codeModal = document.getElementById('code-modal');
const codeModalTitle = document.getElementById('code-modal-title');
const codeModalFilebar = document.getElementById('code-modal-filebar');
const codeModalCode = document.getElementById('code-modal-code');
const codeTriggers = document.querySelectorAll('[data-code-target]');
let codeModalCloseTimeout = null;

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

function openCodeModal(button) {
    const targetId = button.dataset.codeTarget;
    const source = document.getElementById(targetId);

    if (!codeModal || !source || !codeModalTitle || !codeModalFilebar || !codeModalCode) {
        return;
    }

    const fileName = button.dataset.fileLabel || button.querySelector('.repo-file-name')?.textContent || 'File preview';
    const codeLines = source.textContent.split('\n');

    codeModalTitle.textContent = fileName;
    codeModalFilebar.textContent = 'API_Testing_Java_Yakov / ' + fileName;
    codeModalCode.innerHTML = codeLines.map((line, index) => {
        const trimmedLine = line.replace(/^\d+(?:  )?/, '');
        const highlightedCode = renderHighlightedLine(trimmedLine, fileName);

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
    void codeModal.offsetWidth;
    codeModal.classList.add('is-visible');
    document.body.style.overflow = 'hidden';
}

function closeCodeModal() {
    if (!codeModal) {
        return;
    }

    if (codeModal.hidden || codeModal.classList.contains('is-closing')) {
        return;
    }

    codeModal.classList.remove('is-visible');
    codeModal.classList.add('is-closing');

    codeModalCloseTimeout = setTimeout(() => {
        codeModal.hidden = true;
        codeModal.classList.remove('is-closing');
        document.body.style.overflow = '';
        codeModalCloseTimeout = null;
    }, 220);
}

codeTriggers.forEach(button => {
    button.addEventListener('click', () => openCodeModal(button));
});

document.querySelectorAll('[data-close-code-modal]').forEach(element => {
    element.addEventListener('click', closeCodeModal);
});

document.addEventListener('keydown', event => {
    if (event.key === 'Escape') {
        closeCodeModal();
    }
});
