package br.edu.ifpb.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.sis.xml.MarshallerPool;
import org.geotoolkit.csw.CatalogServicesServer;
import org.geotoolkit.csw.GetRecordsRequest;
import org.geotoolkit.csw.xml.CSWMarshallerPool;
import org.geotoolkit.csw.xml.ResultType;
import org.geotoolkit.csw.xml.v202.GetRecordsResponseType;
import org.opengis.metadata.Metadata;

import br.edu.ifpb.daos.MetadataDAO;
import br.edu.ifpb.entidades.Metadatarecord;
import br.edu.ifpb.entidades.Metadatarecordurl;

public class SaveNewMetadata {

	private final MarshallerPool pool;
	private final Unmarshaller um;
	private GetRecordsRequest getRecords;
	private Metadatarecord metadatarecord;
	private MetadataDAO metadataDAO = new MetadataDAO();
	
	public SaveNewMetadata() throws JAXBException {
		metadatarecord = new Metadatarecord();
		pool = CSWMarshallerPool.getInstance();
		um = pool.acquireUnmarshaller();
		
	}

	@SuppressWarnings("unchecked")
	public void save(CatalogServicesServer cswServer, InputStream is,GetRecordsResponseType response) throws IOException {

		getRecords = cswServer.createGetRecords();
		getRecords.setTypeNames("gmd:MD_Metadata");
		getRecords.setConstraintLanguage("CQL");
		getRecords.setConstraintLanguageVersion("1.1.0");
		getRecords.setConstraint("Title like '%'");
		getRecords.setResultType(ResultType.RESULTS);
		is = getRecords.getResponseStream();
		

		int i = 0;
		while (i < response.getSearchResults().getNumberOfRecordsMatched()) {
			is = getRecords.getResponseStream();

			try {

				response = ((JAXBElement<GetRecordsResponseType>) um.unmarshal(is)).getValue();
				
				Iterator iterator = response.getSearchResults().getAny().iterator();
				getRecords.setMaxRecords(10);

				while (iterator.hasNext()) {
					Metadata metadata = (Metadata) iterator.next();
					System.out.println("Entrei");
					metadatarecord.setMetadataidentifier(metadata.getFileIdentifier());
					metadatarecord.setMetadatarecordurls((List<Metadatarecordurl>) metadata.getDistributionInfo().getTransferOptions().iterator().next().getOnLines());
					System.out.println("Aqui !");
					metadatarecord.setKeywords(metadata.getIdentificationInfo().iterator().next().getDescriptiveKeywords().toString());
					System.out.println("Aqui !");
					metadatarecord.setName(metadata.getIdentificationInfo().iterator().next().getCitation().getTitle().toString());
					System.out.println("Aqui !");
					metadatarecord.setPublicationdate(metadata.getDateStamp());
					System.out.println("Aqui !");
					metadatarecord.setPublisher(metadata.getContacts().iterator().next().getIndividualName());
					//metadatarecord.setReationdate();
					
					metadatarecord.setRevisiondate(metadata.getIdentificationInfo().iterator().next().getCitation().getDates().iterator().next().getDate());
					//metadatarecord.setServices(services);
					//metadatarecord.setTemporalextent(temporalextent);
					metadatarecord.setTextdescription(metadata.getIdentificationInfo().iterator().next().getAbstract().toString());
					//metadatarecord.setUpdate(update);
					//metadatarecord.setUrl(url);
					System.out.println("vou gravar");
					metadataDAO.save(metadatarecord);
					System.out.println("Gravei");					
				}
				i++;
			} catch (Exception e) {
				// e.printStackTrace();
			}
			System.out.println("sai");
			//getRecords.setStartPosition(10 + getRecords.getStartPosition());
		}
	}

}