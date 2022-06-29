package br.ce.mjosuel.servicos;

import br.ce.mjosuel.exceptions.FilmeSemEstoqueException;
import br.ce.mjosuel.exceptions.LocadoraException;
import br.ce.mjosuel.entidades.Filme;
import br.ce.mjosuel.entidades.Locacao;
import br.ce.mjosuel.entidades.Usuario;
import br.ce.mjosuel.servicos.LocacaoService;
import br.ce.mjosuel.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.mjosuel.utils.DataUtils.isMesmaData;
import static br.ce.mjosuel.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class LocacaoServiceTest {

    private LocacaoService service;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup(){
        service = new LocacaoService();
    }

    @Test
    public void deveAlugarFilme() throws Exception {

        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        //Assert.assertEquals(locacao.getValor(),5.0,0.01) ;
        //Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        //Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));

        /*Uso de Assertivas - Vai checando erro a erro não tendo visibilidade de todos os erros nas verificações.
        precisando corrigir erro a erro, para descobrir o proximo erro que falhou*/

        //assertThat(locacao.getValor(), is(equalTo(5.0)));
        //assertThat(locacao.getValor(), is(not(6.0)));
        //assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        //assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

        //Uso de Rules - verifica todos os erros que falharam de uma só vez
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(locacao.getValor(), is(not(6.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

        //ação
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveLancarExcecaoAoAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
        //cenario
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

        //ação
        try {
            service.alugarFilme(null, filmes);
            Assert.fail();
        }catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuário vazio"));
        }

        System.out.println("Forma Robusta");
    }

    @Test
    public void deveLancarExcecaoAoAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario 1");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme Vazio");

        //ação
        service.alugarFilme(usuario, null);

        System.out.println("Forma Nova");
    }

    /*
    @Test
    public void devePagar75pctNoFilme3() throws FilmeSemEstoqueException, LocadoraException
    {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList( new Filme("Filme 1", 2, 4.0),
                                            new Filme("Filme 2", 2, 4.0),
                                            new Filme("Filme 3", 2, 4.0));

        //acao
        Locacao resultado = service.alugarFilme(usuario,filmes);

        //verificacao
        assertThat(resultado.getValor(),is(11.0));
    }

    @Test
    public void devePagar50pctNoFilme4() throws FilmeSemEstoqueException, LocadoraException
    {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList( new Filme("Filme 1", 2, 4.0),
                                            new Filme("Filme 2", 2, 4.0),
                                            new Filme("Filme 3", 2, 4.0),
                                            new Filme("Filme 4", 2, 4.0));
        //acao
        Locacao resultado = service.alugarFilme(usuario,filmes);

        //verificacao
        assertThat(resultado.getValor(),is(13.0));
    }

    @Test
    public void devePagar25pctNoFilme5() throws FilmeSemEstoqueException, LocadoraException
    {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList( new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 4", 2, 4.0),
                new Filme("Filme 5", 2, 4.0));
        //acao
        Locacao resultado = service.alugarFilme(usuario,filmes);

        //verificacao
        assertThat(resultado.getValor(),is(14.0));
    }

    @Test
    public void devePagar0pctNoFilme6() throws FilmeSemEstoqueException, LocadoraException
    {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList( new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 4", 2, 4.0),
                new Filme("Filme 5", 2, 4.0),
                new Filme("Filme 6", 2, 4.0));
        //acao
        Locacao resultado = service.alugarFilme(usuario,filmes);

        //verificacao
        assertThat(resultado.getValor(),is(14.0));
    }*/

    @Test
    public void deveDevolverNaSegundaAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException
    {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Usuário 1");
        List<Filme> filmes = Arrays.asList( new Filme("Filme 1", 2, 4.0));

        //acao
        Locacao retorno = service.alugarFilme(usuario,filmes);

        //verificacao
        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);
    }
}