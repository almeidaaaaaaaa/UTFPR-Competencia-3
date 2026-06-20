window.addEventListener('load', () => {
  const nome = sessionStorage.getItem('nomeUsuario') || 'usuário';
  document.getElementById('saudacao').textContent = 'Olá, ' + nome;
  carregarProjetos();
  carregarMinhasSugestoes();
  carregarTodasSugestoes();
});

async function carregarProjetos() {
  const selectEnvio     = document.getElementById('projeto');
  const selectFiltro    = document.getElementById('filtro-projeto-comunidade');

  try {
    const r = await fetch('/api/projetos');
    if (!r.ok) {
      selectEnvio.innerHTML = '<option value="">Erro ao carregar projetos</option>';
      return;
    }
    const projetos = await r.json();

    selectEnvio.innerHTML = '<option value="">Selecione um projeto...</option>';
    selectFiltro.innerHTML = '<option value="">Todos os projetos</option>';

    projetos.forEach(p => {
      const opt1 = document.createElement('option');
      opt1.value = p.id;
      opt1.textContent = p.nome;
      selectEnvio.appendChild(opt1);

      const opt2 = document.createElement('option');
      opt2.value = p.id;
      opt2.textContent = p.nome;
      selectFiltro.appendChild(opt2);
    });
  } catch (e) {
    selectEnvio.innerHTML = '<option value="">Erro: ' + e.message + '</option>';
  }
}

async function carregarMinhasSugestoes() {
  const lista = document.getElementById('lista-sugestoes');
  try {
    const r = await fetch('/api/sugestoes/minhas');
    if (!r.ok) { lista.innerHTML = '<p class="vazio">Erro ao carregar sugestões.</p>'; return; }
    const sugestoes = await r.json();
    if (sugestoes.length === 0) {
      lista.innerHTML = '<p class="vazio">Você ainda não enviou nenhuma sugestão.</p>';
      return;
    }
    lista.innerHTML = '';
    sugestoes.forEach(s => {
      lista.innerHTML += montarCardSugestao(s, false);
    });
  } catch (e) {
    lista.innerHTML = '<p class="vazio">Falha ao carregar: ' + e.message + '</p>';
  }
}

async function carregarTodasSugestoes() {
  const lista     = document.getElementById('lista-todas-sugestoes');
  const projetoId = document.getElementById('filtro-projeto-comunidade').value;

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
      lista.innerHTML += montarCardSugestao(s, true);
    });
  } catch (e) {
    lista.innerHTML = '<p class="vazio">Falha ao carregar: ' + e.message + '</p>';
  }
}

function montarCardSugestao(s, mostrarAutor) {
  const justificativa = s.justificativa
    ? `<p class="sugestao-justificativa">💬 Justificativa do gestor: ${s.justificativa}</p>`
    : '';

  const autor = mostrarAutor
    ? `· Por: ${s.nomeProponente}`
    : '';

  return `
    <div class="sugestao-item">
      <div class="sugestao-header">
        <span class="sugestao-titulo">${s.titulo}</span>
        <span class="badge-status badge-${s.status}">${formatarStatus(s.status)}</span>
      </div>
      <p class="sugestao-desc">${s.descricao}</p>
      <span class="sugestao-meta">Enviada em ${s.dataEnvio} · Projeto: ${s.nomeProje} ${autor}</span>
      ${justificativa}
    </div>`;
}

async function enviarSugestao() {
  const projetoId = document.getElementById('projeto').value;
  const titulo    = document.getElementById('titulo').value.trim();
  const descricao = document.getElementById('descricao').value.trim();
  const res       = document.getElementById('result-sugestao');

  if (!projetoId || !titulo || !descricao) {
    res.className = 'result err';
    res.style.display = 'block';
    res.textContent = 'Preencha todos os campos.';
    return;
  }

  res.className = 'result';
  res.style.display = 'block';
  res.textContent = 'Enviando...';

  const params = new URLSearchParams();
  params.append('idProjeto', projetoId);
  params.append('titulo', titulo);
  params.append('descricao', descricao);

  try {
    const r = await fetch('/api/sugestoes', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params.toString()
    });

    if (r.ok || r.status === 201) {
      res.className = 'result ok';
      res.textContent = 'Sugestão enviada com sucesso!';
      document.getElementById('titulo').value = '';
      document.getElementById('descricao').value = '';
      document.getElementById('projeto').value = '';
      carregarMinhasSugestoes();
      carregarTodasSugestoes();
    } else {
      const text = await r.text();
      res.className = 'result err';
      res.textContent = 'Erro ' + r.status + ': ' + text;
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
