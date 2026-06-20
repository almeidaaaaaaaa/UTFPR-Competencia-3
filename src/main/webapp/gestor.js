window.addEventListener('load', () => {
  const nome = sessionStorage.getItem('nomeUsuario') || 'gestor';
  document.getElementById('saudacao').textContent = 'Olá, ' + nome;
  carregarProjetosFiltro();
  carregarSugestoes();
});

async function carregarProjetosFiltro() {
  const select = document.getElementById('filtro-projeto');
  try {
    const r = await fetch('/api/projetos');
    if (!r.ok) return;
    const projetos = await r.json();
    projetos.forEach(p => {
      const opt = document.createElement('option');
      opt.value = p.id;
      opt.textContent = p.nome;
      select.appendChild(opt);
    });
  } catch (e) {
    console.error('Erro ao carregar projetos:', e);
  }
}

async function carregarSugestoes() {
  const lista    = document.getElementById('lista-sugestoes-gestor');
  const projetoId = document.getElementById('filtro-projeto').value;

  lista.innerHTML = '<p class="vazio">Carregando...</p>';

  const url = projetoId ? '/api/sugestoes?idProjeto=' + projetoId : '/api/sugestoes';

  try {
    const r = await fetch(url);
    if (!r.ok) { lista.innerHTML = '<p class="vazio">Erro ao carregar sugestões.</p>'; return; }
    const sugestoes = await r.json();
    if (sugestoes.length === 0) {
      lista.innerHTML = '<p class="vazio">Nenhuma sugestão encontrada.</p>';
      return;
    }
    lista.innerHTML = '';
    sugestoes.forEach(s => {
      lista.innerHTML += `
        <div class="sugestao-item">
          <div class="sugestao-header">
            <span class="sugestao-titulo">${s.titulo}</span>
            <span class="badge-status badge-${s.status}">${formatarStatus(s.status)}</span>
          </div>
          <p class="sugestao-desc">${s.descricao}</p>
          <span class="sugestao-meta">Enviada em ${s.dataEnvio} · Por: ${s.nomeProponente}</span>
          ${s.justificativa ? `<p class="sugestao-meta" style="margin-top:4px">Justificativa: ${s.justificativa}</p>` : ''}
          <button class="btn-alterar" onclick="abrirModal(${s.id}, '${s.status}')">Alterar status</button>
        </div>`;
    });
  } catch (e) {
    lista.innerHTML = '<p class="vazio">Falha ao carregar: ' + e.message + '</p>';
  }
}

function abrirModal(idSugestao, statusAtual) {
  document.getElementById('modal-id-sugestao').value = idSugestao;
  document.getElementById('modal-status').value = statusAtual;
  document.getElementById('modal-justificativa').value = '';
  document.getElementById('result-modal').style.display = 'none';
  document.getElementById('modal-overlay').style.display = 'flex';
}

function fecharModal() {
  document.getElementById('modal-overlay').style.display = 'none';
}

async function salvarStatus() {
  const id            = document.getElementById('modal-id-sugestao').value;
  const novoStatus    = document.getElementById('modal-status').value;
  const justificativa = document.getElementById('modal-justificativa').value.trim();
  const res           = document.getElementById('result-modal');

  res.className = 'result';
  res.style.display = 'block';
  res.textContent = 'Salvando...';

  const params = new URLSearchParams();
  params.append('id', id);
  params.append('status', novoStatus);
  params.append('justificativa', justificativa);

  try {
    const r = await fetch('/api/sugestoes/status', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params.toString()
    });

    if (r.ok) {
      res.className = 'result ok';
      res.textContent = 'Status atualizado!';
      setTimeout(() => {
        fecharModal();
        carregarSugestoes();
      }, 800);
    } else {
      const text = await r.text();
      res.className = 'result err';
      res.textContent = 'Erro: ' + text;
    }
  } catch (e) {
    res.className = 'result err';
    res.textContent = 'Falha na conexão: ' + e.message;
  }
}

function formatarStatus(status) {
  const map = { PENDENTE: 'Pendente', EM_ANALISE: 'Em Análise', APROVADA: 'Aprovada', RECUSADA: 'Recusada' };
  return map[status] || status;
}

function sair() {
  sessionStorage.clear();
  window.location.href = 'login.html';
}
