package cn.hexiao.flog.test;

//$Id: LobTest.java 11282 2007-03-14 22:05:59Z epbernard $

import java.sql.Timestamp;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.hexiao.flog.entity.Category;
import cn.hexiao.flog.entity.Post;

/**
 * @author Emmanuel Bernard
 */
public class LobTest extends TestCase {
	/*
	public void testSerializableToBlob() throws Exception {
		Book book = new Book();
		Editor editor = new Editor();
		editor.setName( "O'Reilly" );
		book.setEditor( editor );
		book.setCode2( new char[]{'r'} );
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		s.persist( book );
		tx.commit();
		s.close();
		s = openSession();
		tx = s.beginTransaction();
		Book loadedBook = (Book) s.get( Book.class, book.getId() );
		assertNotNull( loadedBook.getEditor() );
		assertEquals( book.getEditor().getName(), loadedBook.getEditor().getName() );
		loadedBook.setEditor( null );
		tx.commit();
		s.close();
		s = openSession();
		tx = s.beginTransaction();
		loadedBook = (Book) s.get( Book.class, book.getId() );
		assertNull( loadedBook.getEditor() );
		tx.commit();
		s.close();

	}



	public void testBlob() throws Exception {
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		CompiledCode cc = new CompiledCode();
		Byte[] header = new Byte[2];
		header[0] = new Byte( (byte) 3 );
		header[1] = new Byte( (byte) 0 );
		cc.setHeader( header );
		int codeSize = 5;
		byte[] full = new byte[codeSize];
		for ( int i = 0; i < codeSize ; i++ ) {
			full[i] = (byte) ( 1 + i );
		}
		cc.setFullCode( full );
		s.persist( cc );
		tx.commit();
		s.close();
		s = openSession();
		tx = s.beginTransaction();
		CompiledCode recompiled = (CompiledCode) s.get( CompiledCode.class, cc.getId() );
		assertEquals( recompiled.getHeader()[1], cc.getHeader()[1] );
		assertEquals( recompiled.getFullCode()[codeSize - 1], cc.getFullCode()[codeSize - 1] );
		tx.commit();
		s.close();
	}

	public void testBinary() throws Exception {
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		CompiledCode cc = new CompiledCode();
		byte[] metadata = new byte[2];
		metadata[0] = (byte) 3;
		metadata[1] = (byte) 0;
		cc.setMetadata( metadata );
		s.persist( cc );
		tx.commit();
		s.close();
		s = openSession();
		tx = s.beginTransaction();
		CompiledCode recompiled = (CompiledCode) s.get( CompiledCode.class, cc.getId() );
		assertEquals( recompiled.getMetadata()[1], cc.getMetadata()[1] );
		tx.commit();
		s.close();
	}
*/
	public void testClob() throws Exception {
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		Post b = new Post();
		b.setContent("dddddddddddddddddddddddddddd");
		Category c = new Category("test","ddd");
		b.setCategory(c);
		b.setPubTime(new Timestamp(System.currentTimeMillis()));
		b.setSummary("summmary .....");
		b.setTitle("title");
		s.persist(c);
		s.persist( b );
		tx.commit();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		Post b2 = (Post) s.get( Post.class, b.getId() );
		assertNotNull( b2 );
		assertEquals( b2.getContent(), b.getContent() );
		assertEquals( b2.getSummary(), b.getSummary() );
		tx.commit();
		s.close();
	}
	public LobTest(String x) {
		super( x );
	}

	@Override
	protected Class[] getMappings() {
		return new Class[]{
				
		};
	}
}
